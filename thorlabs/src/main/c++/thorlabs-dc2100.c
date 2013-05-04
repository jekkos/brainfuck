/****************************************************************************

	Thorlabs DC2100 High Power LED Driver -  VISA instrument driver

 	Copyright: 	Copyright(c) 2009, Thorlabs GmbH (www.thorlabs.com)
 	Author:		Olaf Wohlmann (owohlmann@thorlabs.com)

	Disclaimer:

	This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General Public
	License along with this library; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


 	Header file

 	Date:       	Jan-05-2009
 	Version:    	1.0

	Changelog:		see 'Readme.rtf'

****************************************************************************/
#include "stdafx.h"
#include "visa.h"    // from VXIpnp directory
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "thorlabs-dc2100.h"

/*===========================================================================
 Macros
===========================================================================*/
// versioning
#define VER_MAJOR       				1
#define VER_MINOR       				0
#define VER_SUBMINOR    				0
#define MAKE_VERSION(m,n,s)	      ( (((m) & 0x00000FFF) << 20) | (((n) & 0x00000FFF) << 8) | ((s) & 0x000000FF) )

// timeouts
#define DC2100_TIMEOUT					2000

// communication link
#define DC2100_BAUDRATE    				115200	// Serial Baudrate
#define DC2100_SYNC_DELAY  				0.2      // in seconds

// range definitions
#define INVAL_RANGE(val, min, max) ( ((val)<(min)) || ((val)>(max)) )

// device name
#define DEVICE_NAME						"DC2100"

// conversion
#define A_MA_CONVERSION					1000.0

/*===========================================================================
 Structures
===========================================================================*/
// static error list
typedef struct
{
	ViStatus err;
	ViString descr;
} DC2100_err_descr_t;


// dynamic error list
typedef struct DC2100_errDescrDyn_t  DC2100_errDescrDyn_t;
typedef struct DC2100_errDescrDyn_t
{
	ViStatus             	err;
	ViChar     	          	descr[DC2100_BUFFER_SIZE];
	DC2100_errDescrDyn_t  	*next;
}  DC2100_errDescrDyn_t;


// driver private data
typedef struct
{
   ViChar   				name[DC2100_BUFFER_SIZE]; 			// devices name
	ViChar					serial[DC2100_BUFFER_SIZE];			// serial number
	ViChar					manufacturer[DC2100_BUFFER_SIZE]; // manufacturer
	ViChar					firmware[DC2100_BUFFER_SIZE];		// firmware
	DC2100_errDescrDyn_t  *errList;   							// dynamic error list
} DC2100_data_t;


/*===========================================================================
 Local prototypes
===========================================================================*/
static ViStatus 	DC2100_cleanup (ViPSession pInstr, ViStatus err);
static ViStatus 	DC2100_idQuery (ViSession instr, ViChar _VI_FAR name[], ViChar _VI_FAR serial[], ViChar _VI_FAR firm[], ViChar _VI_FAR manu[]);
static ViStatus 	DC2100_getLastError (ViSession instr, ViPStatus errCode);
static ViBoolean 	DC2100_parseError(ViChar * orgMsg, ViPStatus errCode, ViChar errMsg[]);

// Dynamic error list functions
static void                	DC2100_dynErrlist_free	(DC2100_errDescrDyn_t *list);
static ViStatus            	DC2100_dynErrlist_lookup	(DC2100_errDescrDyn_t *list, ViStatus err, ViChar** descr);
static DC2100_errDescrDyn_t*	DC2100_dynErrlist_add		(DC2100_errDescrDyn_t *list, ViStatus err, ViChar* descr);

// Patches to compile driver without utility.h
int round(float x) { 
	return (floor(x + 0.5)); 
}

ViStatus _VI_FUNC  viWrite         (ViSession vi, ViChar* viChar, ViUInt32 cnt, ViPUInt32 retCnt) {
	// TODO cast char to unsigned char and call write !!
	return viWrite(vi, (ViBuf) *viChar, cnt, retCnt);
}


ViStatus _VI_FUNC  viRead         (ViSession vi, ViChar* viChar, ViUInt32 cnt, ViPUInt32 retCnt) {
	// TODO cast char to unsigned char and call write !!
	return viRead(vi, (ViBuf) *viChar, cnt, retCnt);
}

/*===========================================================================
 USER-CALLABLE FUNCTIONS (Exportable Functions)
===========================================================================*/
/*---------------------------------------------------------------------------
 Initialize
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_init (ViRsrc resourceName, ViBoolean IDQuery, ViBoolean resetDevice, ViPSession pInstr)
{
	ViStatus    	err = VI_SUCCESS;
	ViSession   	rm = VI_NULL;
	DC2100_data_t	*data;
	ViChar			name[DC2100_BUFFER_SIZE];

	ViSession 	instr 	= VI_NULL;

	//Open instrument session
	if ((err = viOpenDefaultRM (&rm)) < 0) return (err);
	if ((err = viOpen (rm, resourceName, /*VI_EXCLUSIVE_LOCK*/VI_NULL, VI_NULL, pInstr)) < 0)
	{
		viClose (rm);
		return (err);
	}
	if((err = viSetAttribute(*pInstr, VI_ATTR_USER_DATA, (ViAttrState)VI_NULL)))
	{
		viClose(*pInstr);
		viClose(rm);
		return (err);
	}

	// Private driver data
	if((data = (DC2100_data_t*)malloc(sizeof(DC2100_data_t))) == NULL) 					               return DC2100_cleanup(pInstr, VI_ERROR_SYSTEM_ERROR);
	if((err = viSetAttribute (*pInstr, VI_ATTR_USER_DATA, (ViAttrState)data)) < 0)               return DC2100_cleanup(pInstr, err);
	data->errList  = (DC2100_errDescrDyn_t*)VI_NULL;

	// Configure Session
	if ((err = viSetAttribute(*pInstr, VI_ATTR_TMO_VALUE,         DC2100_TIMEOUT)) < 0)      		return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute(*pInstr, VI_ATTR_ASRL_BAUD,         DC2100_BAUDRATE)) < 0) 			return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute(*pInstr, VI_ATTR_ASRL_DATA_BITS,    8)) < 0)  							return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute(*pInstr, VI_ATTR_ASRL_PARITY,       VI_ASRL_PAR_NONE)) < 0)  		return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute(*pInstr, VI_ATTR_ASRL_STOP_BITS,    VI_ASRL_STOP_ONE)) < 0)  		return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute(*pInstr, VI_ATTR_ASRL_FLOW_CNTRL,   VI_ASRL_FLOW_RTS_CTS)) < 0) 	return DC2100_cleanup(pInstr, err);

	if ((err = viSetAttribute(*pInstr, VI_ATTR_TERMCHAR,          '\n')) < 0)  						return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute(*pInstr, VI_ATTR_ASRL_END_IN,       VI_ASRL_END_TERMCHAR)) < 0)  	return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute(*pInstr, VI_ATTR_ASRL_END_OUT,      VI_ASRL_END_NONE)) < 0)  		return DC2100_cleanup(pInstr, err);
	//if ((err = viSetAttribute(*pInstr, VI_ATTR_ASRL_DISCARD_NULL, VI_TRUE)) < 0)  				   return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute(*pInstr, VI_ATTR_TERMCHAR_EN,       VI_TRUE)) < 0)  					return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute(*pInstr, VI_ATTR_SEND_END_EN,       VI_FALSE)) < 0)  					return DC2100_cleanup(pInstr, err);

	// Communication buffers
	if ((err = viFlush (*pInstr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)		   return DC2100_cleanup(pInstr, err);
	if ((err = viSetBuf(*pInstr, VI_IO_IN_BUF | VI_IO_OUT_BUF, 640)) < 0)   						   return DC2100_cleanup(pInstr, err);
	if ((err = viSetBuf(*pInstr, VI_READ_BUF    | VI_WRITE_BUF, 4096)) < 0)      				      return DC2100_cleanup(pInstr, err);
	if ((err = viFlush (*pInstr, VI_WRITE_BUF_DISCARD | VI_READ_BUF_DISCARD)) < 0)				   return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute (*pInstr, VI_ATTR_WR_BUF_OPER_MODE, VI_FLUSH_WHEN_FULL)) < 0)      return DC2100_cleanup(pInstr, err);
	if ((err = viSetAttribute (*pInstr, VI_ATTR_RD_BUF_OPER_MODE, VI_FLUSH_DISABLE)) < 0)  	   return DC2100_cleanup(pInstr, err);

   // Sync
   if ((err = viWrite (*pInstr, "\n", 1, VI_NULL)) < 0) 	   		                              return DC2100_cleanup(pInstr, err);
	//Delay(0.2);
	Sleep(2000);
	if ((err = viFlush (*pInstr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)		   return DC2100_cleanup(pInstr, err);
	//Delay(0.2);
	Sleep(2000);
	// Info query
	if ((err = DC2100_idQuery (*pInstr, data->name, data->serial, data->firmware, data->manufacturer)) < 0)	 return DC2100_cleanup(pInstr, VI_ERROR_FAIL_ID_QUERY);

	// is it a DC2100?
	if(!(strstr(data->name, DEVICE_NAME)))																			return DC2100_cleanup(pInstr, VI_ERROR_FAIL_ID_QUERY);

   return (VI_SUCCESS);
}


/*---------------------------------------------------------------------------
 Set user limit current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_setLimitCurrent (ViSession instr, ViReal32 limit)
{
	ViStatus err, ret;
	ViChar	buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	// prepare the setting command
	len = sprintf(buf, "L %d\n", (ViUInt16)(round(limit * A_MA_CONVERSION)));
	// transmit the setting command
	if((err = viWrite(instr, buf, len, VI_NULL)) < 0)	return (err);
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))				return(err);
	// return error code
	return (ret);
}



/*---------------------------------------------------------------------------
 Set user limit current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getLimitCurrent (ViSession instr, ViPReal32 limit)
{
	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];
	ViInt32	len;
	ViUInt16	tmp = 0;

	// flush
	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   // prepare the command
	len = sprintf(buf, "L?\n");
	// write command
	if((err = viWrite (instr, buf, len, VI_NULL)) < 0) 			                        return (err);
   // readback answer
	if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(limit)
	{
		// parse answer
		if(sscanf(buf, " %d \n", &tmp) < 1)                                      			return (VI_ERROR_INV_RESPONSE);
		*limit = (ViReal32)tmp / A_MA_CONVERSION;
	}
   // ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}

/*---------------------------------------------------------------------------
 Set maximum current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_setMaxLimit (ViSession instr, ViReal32 limit)
{
	ViStatus err, ret;
	ViChar	buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	// prepare the setting command
	len = sprintf(buf, "ML %d\n", (ViUInt16)(round(limit * A_MA_CONVERSION)));
	// transmit the setting command
	if((err = viWrite(instr, buf, len, VI_NULL)) < 0)	return (err);
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))				return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get maximum current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getMaxLimit (ViSession instr, ViPReal32 limit)
{
 	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];
	ViInt32	len;
	ViUInt16	tmp = 0;

	// flush
	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   // prepare the command
	len = sprintf(buf, "ML?\n");
	// write command
	if((err = viWrite (instr, buf, len, VI_NULL)) < 0) 			                        return (err);
   // readback answer
	if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(limit)
	{
		// parse answer
		if(sscanf(buf, " %d \n", &tmp) < 1)                               					return (VI_ERROR_INV_RESPONSE);
		*limit = (ViReal32)tmp / A_MA_CONVERSION;
   }
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Set operation mode.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_setOperationMode (ViSession instr, ViInt32 operationMode)
{
	ViStatus	err, ret;
	ViChar	buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	len = sprintf(buf, "M %d\n", operationMode);
	if((err = viWrite(instr, buf, len, VI_NULL)) < 0)                 return (err);
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))									return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get operation mode.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getOperationMode (ViSession instr, ViPInt32 operationMode)
{
	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];

	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   if((err = viWrite (instr, "M?\n", 3, VI_NULL)) < 0) 			                        return (err);
   if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(operationMode)
	{
		if(sscanf(buf, " %d \n", operationMode) < 1)                                     return (VI_ERROR_INV_RESPONSE);
   }
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Set LED output.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_setLedOnOff (ViSession instr, ViBoolean LEDOnOff)
{
 	ViStatus	err, ret;
	ViChar	buf[DC2100_BUFFER_SIZE];
	ViInt32	len = 0;

	if(LEDOnOff)
	{
		len = sprintf(buf, "O 1\n");
	}
	else
	{
		len = sprintf(buf, "O 0\n");
	}

	if((err = viWrite(instr, buf, len, VI_NULL)) < 0)                 return (err);
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))									return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get LED output.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getLedOnOff (ViSession instr, ViPBoolean LEDOutputState)
{
	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];
	ViInt32  tmp;
	ViInt32	len = 0;

	// flush
	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   // prepare the command
	len = sprintf(buf, "O?\n");
	if((err = viWrite (instr, buf, len, VI_NULL)) < 0) 			                        return (err);
   if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(sscanf(buf, " %d \n", &tmp) < 1)                                       				return (VI_ERROR_INV_RESPONSE);
	if((tmp) && (LEDOutputState))
	{
		*LEDOutputState = VI_TRUE;
	}
	else
	{
		*LEDOutputState = VI_FALSE;
	}

	// ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Set constant current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_setConstCurrent (ViSession instr, ViReal32 current)
{
	ViStatus err, ret;
	ViChar	buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	// prepare the setting command
	len = sprintf(buf, "CC %d\n", (ViUInt16)(round(current * A_MA_CONVERSION)));
	// transmit the setting command
	if((err = viWrite(instr, buf, len, VI_NULL)) < 0)	return (err);
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))				return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get constant current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getConstCurrent (ViSession instr, ViPReal32 current)
{
 	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];
	ViInt32	len;
	ViUInt16	tmp = 0;

	// flush
	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   // prepare the command
	len = sprintf(buf, "CC?\n");
	// write command
	if((err = viWrite (instr, buf, len, VI_NULL)) < 0) 			                        return (err);
   // readback answer
	if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(current)
	{
		// parse answer
		if(sscanf(buf, " %d \n", &tmp) < 1)                                   				return (VI_ERROR_INV_RESPONSE);
		*current = (ViReal32)tmp / A_MA_CONVERSION;
	}
   // ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Set PWM current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_setPWMCurrent (ViSession instr, ViReal32 current)
{
	ViStatus err, ret;
	ViChar	buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	// prepare the setting command
	len = sprintf(buf, "PC %d\n", (ViUInt16)(round(current * A_MA_CONVERSION)));
	// transmit the setting command
	if((err = viWrite(instr, buf, len, VI_NULL)) < 0)	return (err);
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))				return(err);
	// return error code
	return (ret);
}



/*---------------------------------------------------------------------------
 Get PWM current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getPWMCurrent (ViSession instr, ViPReal32 current)
{
 	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];
	ViInt32	len;
	ViUInt16	tmp = 0;

	// flush
	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   // prepare the command
	len = sprintf(buf, "PC?\n");
	// write command
	if((err = viWrite (instr, buf, len, VI_NULL)) < 0) 			                        return (err);
   // readback answer
	if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(current)
	{
		// parse answer
		if(sscanf(buf, " %d \n", &tmp) < 1)                                   				return (VI_ERROR_INV_RESPONSE);
		*current = (ViReal32)tmp / A_MA_CONVERSION;
	}
   // ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Set PWM frequency.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_setPWMFrequency (ViSession instr, ViInt32 frequency)
{
	ViStatus	err, ret;
	ViChar	buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	len = sprintf(buf, "PF %d\n", frequency);
	if((err = viWrite(instr, buf, len, VI_NULL)) < 0)                 return (err);
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))									return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get PWM frequency.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getPWMFrequency (ViSession instr, ViPInt32 frequency)
{
	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];

	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   if((err = viWrite (instr, "PF?\n", 4, VI_NULL)) < 0) 			                        return (err);
   if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(frequency)
	{
		if(sscanf(buf, " %d \n", frequency) < 1)                                     		return (VI_ERROR_INV_RESPONSE);
   }
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Set PWM duty cycle.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_setPWMDutyCycle (ViSession instr, ViInt32 dutyCycle)
{
	ViStatus	err, ret;
	ViChar	buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	len = sprintf(buf, "PD %d\n", dutyCycle);
	if((err = viWrite(instr, buf, len, VI_NULL)) < 0)                 return (err);
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))									return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get PWM duty cycle.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getPWMDutyCycle (ViSession instr, ViPInt32 dutyCycle)
{
	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];

	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   if((err = viWrite (instr, "PD?\n", 4, VI_NULL)) < 0) 			                        return (err);
   if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(dutyCycle)
	{
		if(sscanf(buf, " %d \n", dutyCycle) < 1)                                     		return (VI_ERROR_INV_RESPONSE);
   }
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Set PWM counts.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_setPWMCounts (ViSession instr, ViInt32 counts)
{
	ViStatus	err, ret;
	ViChar	buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	len = sprintf(buf, "PN %d\n", counts);
	if((err = viWrite(instr, buf, len, VI_NULL)) < 0)                 return (err);
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))									return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get PWM counts.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getPWMCounts (ViSession instr, ViPInt32 counts)
{
	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];

	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   if((err = viWrite (instr, "PN?\n", 4, VI_NULL)) < 0) 			                        return (err);
   if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(counts)
	{
		if(sscanf(buf, " %d \n", counts) < 1)                                     			return (VI_ERROR_INV_RESPONSE);
   }
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Set the display brightness.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_setDispBright (ViSession instr, ViInt32 displayBrightness)
{
	ViStatus	err, ret;
	ViChar	buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	len = sprintf(buf, "B %d\n", displayBrightness);
	if((err = viWrite(instr, buf, len, VI_NULL)) < 0)                							return (err);
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get the display brightness.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getDispBright (ViSession instr, ViPInt32 displayBrightness)
{
	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];

	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   if((err = viWrite (instr, "B?\n", 3, VI_NULL)) < 0) 			                        return (err);
   if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(displayBrightness)
	{
		if(sscanf(buf, " %d \n", displayBrightness) < 1)                                 return (VI_ERROR_INV_RESPONSE);
   }
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get the error register.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getStatusRegister (ViSession instr, ViPInt32 statusRegister)
{
	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];

	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   if((err = viWrite (instr, "R?\n", 3, VI_NULL)) < 0) 			                        return (err);
   if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(statusRegister)
	{
		if(sscanf(buf, " %d \n", statusRegister) < 1)                                    return (VI_ERROR_INV_RESPONSE);
   }
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get error message.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_errorMessage (ViSession instr, ViStatus stat, ViChar _VI_FAR msg[])
{
	ViStatus				err;
	ViChar	         *str;
	DC2100_data_t      *data;
	const DC2100_err_descr_t *ptr;


	static DC2100_err_descr_t err_msg_list[] =
	{
		{VI_ERROR_PARAMETER1,         "Parameter 1 out of range" 										},
		{VI_ERROR_PARAMETER2,         "Parameter 2 out of range" 										},
		{VI_ERROR_PARAMETER3,         "Parameter 3 out of range" 										},
		{VI_ERROR_PARAMETER4,         "Parameter 4 out of range" 										},
		{VI_ERROR_PARAMETER5,         "Parameter 5 out of range" 										},
		{VI_ERROR_PARAMETER6,         "Parameter 6 out of range" 										},
		{VI_ERROR_PARAMETER7,         "Parameter 7 out of range" 										},
		{VI_ERROR_PARAMETER8,         "Parameter 8 out of range"											},
		{VI_ERROR_FAIL_ID_QUERY,      "Instrument identification query failed"            		},
		{VI_ERROR_INV_RESPONSE,       "Errors occured interpreting instrument's response"      },
		{VI_ERROR_GET_INSTR_ERROR,    "Errors occured querying the instrument's Error Queue"   },
		{VI_ERROR_UNKNOWN_ATTRIBUTE,  "Tried to use a unknown attribute" 							 	},
		{VI_ERROR_NOT_SUPPORTED,  		"The desired operation is not possible or supported" 	 	},

		{0 , VI_NULL}
	};

	if(!msg)	return VI_SUCCESS;

	// VISA errors
	if(viStatusDesc(instr, stat, msg) != VI_WARN_UNKNOWN_STATUS) return (VI_SUCCESS);

	// Static driver errors
	ptr = err_msg_list;
	while(ptr->descr != VI_NULL)
	{
		if(ptr->err == stat)
		{
   		strncpy(msg, ptr->descr, DC2100_ERR_DESCR_BUFFER_SIZE);
   		return (VI_SUCCESS);
		}
		ptr ++;
	}

	// Dynamic instrument errors
	if((err = viGetAttribute(instr, VI_ATTR_USER_DATA, &data)) != VI_SUCCESS) return (err);
	if((err = DC2100_dynErrlist_lookup(data->errList, stat, &str)) == VI_SUCCESS)
	{
		strcpy(msg, str);
		return VI_SUCCESS;
	}

   // Not found
	viStatusDesc(instr, VI_WARN_UNKNOWN_STATUS, msg);
	return (VI_WARN_UNKNOWN_STATUS);
}


/*---------------------------------------------------------------------------
 Identification query.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_identificationQuery (ViSession instr, ViChar _VI_FAR manufacturerName[], ViChar _VI_FAR deviceName[], ViChar _VI_FAR serialNumber[], ViChar _VI_FAR firmwareRevision[])
{
	ViStatus    err;
	DC2100_data_t  *data;

	// get private data
	if((err = viGetAttribute(instr, VI_ATTR_USER_DATA, &data)) < 0)   return (err);
	// device name
   if(deviceName) strcpy(deviceName, data->name);
	// manufacturer
   if(manufacturerName) strcpy(manufacturerName, data->manufacturer);
	// serial number
   if(serialNumber) strcpy(serialNumber, data->serial);
	// firmware
   if(firmwareRevision) strcpy(firmwareRevision, data->firmware);

	return (VI_SUCCESS);
}


/*---------------------------------------------------------------------------
 Revision query.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_revisionQuery (ViSession instr, ViPInt32 instrumentDriverRevision, ViChar firmwareRevision[])
{
	ViStatus    err;
	DC2100_data_t  *data;

	if(firmwareRevision)
	{
		// get private data
		if((err = viGetAttribute(instr, VI_ATTR_USER_DATA, &data)) < 0)   return (err);
	}

	// firmware
   if(firmwareRevision) strcpy(firmwareRevision, data->firmware);
	if(instrumentDriverRevision) *instrumentDriverRevision = MAKE_VERSION(VER_MAJOR, VER_MINOR, VER_SUBMINOR);

	return (VI_SUCCESS);
}


/*---------------------------------------------------------------------------
 LED head identification query.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getHeadInfo (ViSession instr, ViChar _VI_FAR serialNumber[], ViChar _VI_FAR name[], ViPInt32 type)
{
 	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	// flush
	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   if(serialNumber)
	{
		// prepare the command to get the serial number
		len = sprintf(buf, "HS?\n");
		// write command
		if((err = viWrite (instr, buf, len, VI_NULL)) < 0) 			                        return (err);
	   // readback answer
		if((err = viRead (instr, serialNumber, DC2100_BUFFER_SIZE - 1, VI_NULL)) < 0) 	      return (err);
	   // ask for error
		if(err = DC2100_getLastError(instr, &ret))															return(err);
	}
	if(name)
	{
		// prepare the command to get the serial number
		len = sprintf(buf, "HN?\n");
		// write command
		if((err = viWrite (instr, buf, len, VI_NULL)) < 0) 			                        return (err);
	   // readback answer
		if((err = viRead (instr, name, DC2100_BUFFER_SIZE - 1, VI_NULL)) < 0) 	      		return (err);
	   // ask for error
		if(err = DC2100_getLastError(instr, &ret))															return(err);
	}
	if(type)
	{
		// prepare command to get the type
		len = sprintf(buf, "T?\n");
		// write command
		if((err = viWrite (instr, buf, len, VI_NULL)) < 0) 			                        return (err);
		// readback answer
		if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	      						return (err);
		// parse answer
		if(sscanf(buf, " %d \n", type) < 1)                                  					return (VI_ERROR_INV_RESPONSE);
   }
	// ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get the wavelength information.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getWavelength (ViSession instr, ViPReal32 wavelength)
{
 	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	// flush
	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   // prepare the command
	len = sprintf(buf, "WL?\n");
	// write command
	if((err = viWrite (instr, buf, len, VI_NULL)) < 0) 			                        return (err);
   // readback answer
	if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(wavelength)
	{
		// parse answer
		if(sscanf(buf, " %e \n", wavelength) < 1)                                   		return (VI_ERROR_INV_RESPONSE);
	}
   // ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Get the forward bias.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_getForwardBias (ViSession instr, ViPReal32 forwardBias)
{
 	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];
	ViInt32	len;

	// flush
	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   // prepare the command
	len = sprintf(buf, "FB?\n");
	// write command
	if((err = viWrite (instr, buf, len, VI_NULL)) < 0) 			                        return (err);
   // readback answer
	if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(forwardBias)
	{
		// parse answer
		if(sscanf(buf, " %e \n", forwardBias) < 1)                                   		return (VI_ERROR_INV_RESPONSE);
	}
   // ask for error
	if(err = DC2100_getLastError(instr, &ret))															return(err);
	// return error code
	return (ret);
}


/*---------------------------------------------------------------------------
 Close
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC DC2100_close (ViSession instr)
{
	return DC2100_cleanup(&instr, VI_SUCCESS);
}


/*===========================================================================
 UTILITY ROUTINES (Non-Exportable Functions)
===========================================================================*/
/*---------------------------------------------------------------------------
 Clean Up
---------------------------------------------------------------------------*/
static ViStatus DC2100_cleanup(ViPSession pInstr, ViStatus err)
{
	ViSession 	rm;
	ViStatus		stat;
	DC2100_data_t  *data;

	if((stat = viGetAttribute(*pInstr, VI_ATTR_RM_SESSION, &rm)) < 0) return (stat);
	if(viGetAttribute(*pInstr, VI_ATTR_USER_DATA, &data) == VI_SUCCESS)
	{
		DC2100_dynErrlist_free(data->errList);
		if(data) free(data);
	}
	stat = viClose(*pInstr);
	*pInstr = VI_NULL;
	viClose (rm);
	return (err ? err : stat);
}


/*---------------------------------------------------------------------------
 ID Query
---------------------------------------------------------------------------*/
static ViStatus DC2100_idQuery (ViSession instr, ViChar _VI_FAR name[], ViChar _VI_FAR serial[], ViChar _VI_FAR firm[], ViChar _VI_FAR manu[])
{
	ViStatus err, ret;
	ViChar   buf[DC2100_BUFFER_SIZE];

   //Query serial number
   if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
  	if((err = viWrite (instr, "S?\n", 3, VI_NULL)) < 0) 			                        return (err);
  	if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   // das stimmt noch nicht
	if(sscanf(buf, " %[^\r\n]", serial) < 1)                                         return (VI_ERROR_INV_RESPONSE);

   // Device name
  	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
  	if((err = viWrite (instr, "N?\n", 3, VI_NULL)) < 0) 			                        return (err);
  	if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(sscanf(buf, " %[^\r\n]", name) < 1)                                           return (VI_ERROR_INV_RESPONSE);

	// Firmware version
  	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
  	if((err = viWrite (instr, "V?\n", 3, VI_NULL)) < 0) 			                        return (err);
  	if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(sscanf(buf, " %[^\r\n]", firm) < 1)                                           return (VI_ERROR_INV_RESPONSE);

	// Manufacturer
  	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
  	if((err = viWrite (instr, "H?\n", 3, VI_NULL)) < 0) 			                        return (err);
  	if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);
   if(sscanf(buf, " %[^\r\n]", manu) < 1)                                           return (VI_ERROR_INV_RESPONSE);

	return (VI_SUCCESS);
}


/*===========================================================================
 Functions for managing the drivers dynamically allocated error list.
===========================================================================*/
/*---------------------------------------------------------------------------
 Free the dynamic error list.
---------------------------------------------------------------------------*/
static void DC2100_dynErrlist_free(DC2100_errDescrDyn_t *list)
{
	DC2100_errDescrDyn_t *next;

	while(list != NULL)
	{
		next = list->next;
		free(list);
		list = next;
	}
}


/*---------------------------------------------------------------------------
 Searches for an entry in the dynamic error list specified by a status code.
---------------------------------------------------------------------------*/
static ViStatus DC2100_dynErrlist_lookup(DC2100_errDescrDyn_t *list, ViStatus err, ViChar** pDescr)
{
	while(list != VI_NULL)
	{
		if(list->err == err)
		{
			if(pDescr != VI_NULL) *pDescr = list->descr;
			return (VI_SUCCESS);
		}
		list = list->next;
	}
	return (VI_WARN_UNKNOWN_STATUS);
}


/*---------------------------------------------------------------------------
 Adds an entry to the dynamic error list in case the error is not already there.
---------------------------------------------------------------------------*/
static DC2100_errDescrDyn_t *DC2100_dynErrlist_add(DC2100_errDescrDyn_t *list, ViStatus err, ViChar* descr)
{
	DC2100_errDescrDyn_t  *new;

	// Is error already in list?
	if(DC2100_dynErrlist_lookup(list, err, VI_NULL) == VI_SUCCESS) return (list);
	// Add new        {
	if((new = (DC2100_errDescrDyn_t*)malloc(sizeof(DC2100_errDescrDyn_t))) == VI_NULL) return (list);
	new->next = list;
	new->err = err;
	strncpy(new->descr, descr, DC2100_BUFFER_SIZE);
	return new;
}


/*---------------------------------------------------------------------------
 Gets the error code from the last command and adds it to list if necessary.
---------------------------------------------------------------------------*/
static ViStatus DC2100_getLastError (ViSession instr, ViPStatus errCode)
{
 	ViStatus err;
	ViChar   buf[DC2100_BUFFER_SIZE];
	ViChar	errMsg[DC2100_ERR_DESCR_BUFFER_SIZE];
	DC2100_data_t * data;

	if((err = viFlush (instr, VI_ASRL_IN_BUF_DISCARD | VI_ASRL_OUT_BUF_DISCARD)) < 0)   return (err);
   if((err = viWrite (instr, "E?\n", 3, VI_NULL)) < 0) 			                        return (err);
   if((err = viRead (instr, buf, sizeof(buf), VI_NULL)) < 0) 	                        return (err);

	// parse for error
	if(DC2100_parseError(buf, errCode, errMsg) != VI_TRUE)											return (VI_ERROR_INV_RESPONSE);

	if(*errCode == VI_SUCCESS)																				return (VI_SUCCESS);

	// Get private data
	if((err = viGetAttribute(instr, VI_ATTR_USER_DATA, &data))) return (err);
	// calculate our error code
	*errCode += VI_INSTR_ERROR_OFFSET;
	// Add instrument error to dynamic error list
	data->errList = DC2100_dynErrlist_add(data->errList, *errCode, errMsg);

	return (VI_SUCCESS);
}


/*---------------------------------------------------------------------------
 Parses the error string.
---------------------------------------------------------------------------*/
static ViBoolean DC2100_parseError(ViChar * orgMsg, ViPStatus errCode, ViChar errMsg[])
{
	if(orgMsg == NULL)
		return VI_FALSE;

	if(sscanf(orgMsg, "ERROR %d:%[^\n]", errCode, errMsg) < 2)	return VI_FALSE;

	return VI_TRUE;
}


/****************************************************************************

  End of Source file

****************************************************************************/
