/****************************************************************************

	Thorlabs DC2100 - High Power LED Driver -  VISA instrument driver

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

 	Date:       	Mar-25-2009
 	Version:    	1.0

	Changelog:		see 'Readme.rtf'

****************************************************************************/

#ifndef _TLDC2100_HEADER_
#define _TLDC2100_HEADER_

#include "vpptype.h"

#if defined(__cplusplus) || defined(__cplusplus__)
extern "C"
{
#endif

/*---------------------------------------------------------------------------
 Buffers
---------------------------------------------------------------------------*/
#define DC2100_BUFFER_SIZE               256      // General buffer size
#define DC2100_ERR_DESCR_BUFFER_SIZE     512      // Error description buffer size

/*---------------------------------------------------------------------------
 Find Pattern
---------------------------------------------------------------------------*/
#define DC2100_FIND_PATTERN		      	"ASRL?*"

/*---------------------------------------------------------------------------
 Error/Warning Codes
---------------------------------------------------------------------------*/
// Offsets
#define VI_INSTR_WARNING_OFFSET                   	(0x3FFC0900L)
#define VI_INSTR_ERROR_OFFSET          (_VI_ERROR + 0x3FFC0900L)	//0xBFFC0900
// Driver Error Codes
#define VI_ERROR_GET_INSTR_ERROR       (_VI_ERROR + 0x3FFC0805L)

#define VI_ERROR_UNKNOWN_ATTRIBUTE		(VI_ERROR_GET_INSTR_ERROR + 1)
#define VI_ERROR_NOT_SUPPORTED			(VI_ERROR_GET_INSTR_ERROR + 2)


/*---------------------------------------------------------------------------
 Register Values
---------------------------------------------------------------------------*/
// common status bits
#define STAT_VCC_FAIL_CHANGED				0x0001
#define STAT_VCC_FAIL						0x0002
#define STAT_OTP_CHANGED					0x0004
#define STAT_OTP								0x0008
#define STAT_NO_LED1_CHANGED				0x0010
#define STAT_NO_LED1							0x0020
#define STAT_LED_OPEN1_CHANGED			0x0040
#define STAT_LED_OPEN1						0x0080
#define STAT_LED_LIMIT1_CHANGED			0x0100
#define STAT_LED_LIMIT1						0x0200
#define STAT_IFC_REFRESH_CHANGED			0x1000


/*---------------------------------------------------------------------------
 Operation modes
---------------------------------------------------------------------------*/
#define MODUS_CONST_CURRENT		0
#define MODUS_PWM						1
#define MODUS_EXTERNAL_CONTROL	2

/*---------------------------------------------------------------------------
 LED head types
---------------------------------------------------------------------------*/
#define NO_HEAD						0		// no head at all
#define FOUR_CHANNEL_HEAD			1		// four channel head
#define ONE_CHANNEL_HEAD			2		// single channel head
#define NOT_SUPPORTED_HEAD			253	// head with unsupported forward bias
#define UNKNOWN_HEAD					254	// head with unknown type
#define HEAD_WITHOUT_EEPROM		255	// old standard heads

/*---------------------------------------------------------------------------
 GLOBAL USER-CALLABLE FUNCTION DECLARATIONS (Exportable Functions)
---------------------------------------------------------------------------*/


/****************************************************************************
End of Header file
****************************************************************************/

/*---------------------------------------------------------------------------
 Initialize.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_init (ViRsrc resourceName, ViBoolean IDQuery, ViBoolean resetDevice, ViPSession instrumentHandle);

/*---------------------------------------------------------------------------
 Set/Get user limit current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_setLimitCurrent (ViSession instrumentHandle, ViReal32 limit);
ViStatus _VI_FUNC TLDC2100_getLimitCurrent (ViSession instrumentHandle, ViPReal32 limit);

/*---------------------------------------------------------------------------
 Set/Get maximum current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_setMaxLimit (ViSession instrumentHandle, ViReal32 limit);
ViStatus _VI_FUNC TLDC2100_getMaxLimit (ViSession instrumentHandle, ViPReal32 limit);

/*---------------------------------------------------------------------------
 Set/Get operation mode.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_setOperationMode (ViSession instrumentHandle, ViInt32 operationMode);
ViStatus _VI_FUNC TLDC2100_getOperationMode (ViSession instrumentHandle, ViPInt32 operationMode);

/*---------------------------------------------------------------------------
 Set/Get LED output.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_setLedOnOff (ViSession instrumentHandle, ViBoolean LEDOnOff);
ViStatus _VI_FUNC TLDC2100_getLedOnOff (ViSession instrumentHandle, ViPBoolean LEDOutputState);

/*---------------------------------------------------------------------------
 Set/Get constant current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_setConstCurrent (ViSession instrumentHandle, ViReal32 current);
ViStatus _VI_FUNC TLDC2100_getConstCurrent (ViSession instrumentHandle, ViPReal32 current);

/*---------------------------------------------------------------------------
 Set/Get PWM current.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_setPWMCurrent (ViSession instrumentHandle, ViReal32 current);
ViStatus _VI_FUNC TLDC2100_getPWMCurrent (ViSession instrumentHandle, ViPReal32 current);

/*---------------------------------------------------------------------------
 Set/Get PWM frequency.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_setPWMFrequency (ViSession instrumentHandle, ViInt32 frequency);
ViStatus _VI_FUNC TLDC2100_getPWMFrequency (ViSession instrumentHandle, ViPInt32 frequency);

/*---------------------------------------------------------------------------
 Set/Get PWM duty cycle.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_setPWMDutyCycle (ViSession instrumentHandle, ViInt32 dutyCycle);
ViStatus _VI_FUNC TLDC2100_getPWMDutyCycle (ViSession instrumentHandle, ViPInt32 dutyCycle);

/*---------------------------------------------------------------------------
 Set/Get PWM counts.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_setPWMCounts (ViSession instrumentHandle, ViInt32 counts);
ViStatus _VI_FUNC TLDC2100_getPWMCounts (ViSession instrumentHandle, ViPInt32 counts);

/*---------------------------------------------------------------------------
 Set/Get the display brightness.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_setDispBright (ViSession instrumentHandle, ViInt32 displayBrightness);
ViStatus _VI_FUNC TLDC2100_getDispBright (ViSession instrumentHandle, ViPInt32 displayBrightness);

/*---------------------------------------------------------------------------
 Get the status register.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_getStatusRegister (ViSession instrumentHandle, ViPInt32 statusRegister);

/*---------------------------------------------------------------------------
 Get error code.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_error_query (ViSession instrumentHandle,
                                        ViInt32 *errorCode,
                                        ViChar errorMessage[]);

/*---------------------------------------------------------------------------
 Get error message.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_error_message (ViSession instrumentHandle, ViStatus statusCode, ViChar _VI_FAR message[]);

/*---------------------------------------------------------------------------
 Identification query.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_identificationQuery (ViSession instrumentHandle, ViChar _VI_FAR manufacturerName[], ViChar _VI_FAR deviceName[],
															ViChar _VI_FAR serialNumber[], ViChar _VI_FAR firmwareRevision[]);

/*---------------------------------------------------------------------------
Do the Self Test.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_self_test (ViSession instrumentHandle,
                                      ViInt16 *selfTestResult,
                                      ViChar selfTestMessage[]);

/*---------------------------------------------------------------------------
 Reset
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_reset (ViSession instrumentHandle);

/*---------------------------------------------------------------------------
 Revision query.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_revision_query (ViSession instrumentHandle, ViPInt32 instrumentDriverRevision, ViChar _VI_FAR firmwareRevision[]);

/*---------------------------------------------------------------------------
 LED head identification query.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_getHeadInfo (ViSession instrumentHandle, ViChar _VI_FAR serialNumber[], ViChar _VI_FAR name[], ViPInt32 type);

/*---------------------------------------------------------------------------
 Get the wavelength information.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_getWavelength (ViSession instrumentHandle, ViPReal32 wavelength);

/*---------------------------------------------------------------------------
 Get the forward bias.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_getForwardBias (ViSession instrumentHandle, ViPReal32 forwardBias);

/*---------------------------------------------------------------------------
 Close.
---------------------------------------------------------------------------*/
ViStatus _VI_FUNC TLDC2100_close (ViSession instrumentHandle);



#if defined(__cplusplus) || defined(__cplusplus__)
}
#endif

#endif	/* _DC2100_HEADER_ */
