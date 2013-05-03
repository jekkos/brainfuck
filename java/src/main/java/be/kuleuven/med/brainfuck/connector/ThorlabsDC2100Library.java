package be.kuleuven.med.brainfuck.connector;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.WString;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.ShortByReference;
import com.sun.jna.win32.StdCallLibrary;
/**
 * JNA Wrapper for library <b>DC2100_Drv_32</b><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class ThorlabsDC2100Library implements StdCallLibrary {
	public static String JNA_LIBRARY_NAME = "DC2100_Drv_32";
	public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(ThorlabsDC2100Library.JNA_LIBRARY_NAME);
	
	static {
		Native.register(ThorlabsDC2100Library.JNA_LIBRARY_NAME);
	}
	
	public static final int VI_WARN_NSUP_ID_QUERY = (int)(0x3FFC0101);
	public static final int STAT_LED_OPEN1_CHANGED = (int)0x0040;
	public static final int VI_ERROR_NOT_SUPPORTED = (int)(((-2147483647 - 1) + 0x3FFC0805) + 2);
	public static final int STAT_LED_LIMIT1_CHANGED = (int)0x0100;
	public static final int VI_ERROR_GET_INSTR_ERROR = (int)((-2147483647 - 1) + 0x3FFC0805);
	public static final int FOUR_CHANNEL_HEAD = (int)1;
	public static final int ONE_CHANNEL_HEAD = (int)2;
	public static final int UNKNOWN_HEAD = (int)254;
	public static final int VI_ERROR_FAIL_ID_QUERY = (int)((-2147483647 - 1) + 0x3FFC0011);
	public static final int MODUS_EXTERNAL_CONTROL = (int)2;
	public static final int VI_WARN_NSUP_RESET = (int)(0x3FFC0102);
	public static final int MODUS_PWM = (int)1;
	public static final int NO_HEAD = (int)0;
	public static final int STAT_NO_LED1 = (int)0x0020;
	public static final int VI_ERROR_INV_RESPONSE = (int)((-2147483647 - 1) + 0x3FFC0012);
	public static final int VI_TRUE = (int)(1);
	public static final int STAT_IFC_REFRESH_CHANGED = (int)0x1000;
	public static final int DC2100_BUFFER_SIZE = (int)256;
	public static final int VI_ON = (int)(1);
	public static final int VI_INSTR_WARNING_OFFSET = (int)(0x3FFC0900);
	public static final int VI_WARN_NSUP_SELF_TEST = (int)(0x3FFC0103);
	public static final int VI_FALSE = (int)(0);
	public static final int VI_WARN_NSUP_REV_QUERY = (int)(0x3FFC0105);
	public static final int VI_OFF = (int)(0);
	public static final int VI_WARN_NSUP_ERROR_QUERY = (int)(0x3FFC0104);
	public static final int VI_SUCCESS = (int)(0);
	public static final int VI_ERROR_UNKNOWN_ATTRIBUTE = (int)(((-2147483647 - 1) + 0x3FFC0805) + 1);
	public static final int NOT_SUPPORTED_HEAD = (int)253;
	public static final int VI_ERROR_PARAMETER1 = (int)((-2147483647 - 1) + 0x3FFC0001);
	public static final int STAT_LED_OPEN1 = (int)0x0080;
	public static final int VI_ERROR_PARAMETER5 = (int)((-2147483647 - 1) + 0x3FFC0005);
	public static final int VI_ERROR_PARAMETER4 = (int)((-2147483647 - 1) + 0x3FFC0004);
	public static final int VI_ERROR_PARAMETER3 = (int)((-2147483647 - 1) + 0x3FFC0003);
	public static final int VI_ERROR_PARAMETER2 = (int)((-2147483647 - 1) + 0x3FFC0002);
	public static final int STAT_VCC_FAIL = (int)0x0002;
	public static final int VI_ERROR_PARAMETER8 = (int)((-2147483647 - 1) + 0x3FFC0008);
	public static final int VI_ERROR_PARAMETER6 = (int)((-2147483647 - 1) + 0x3FFC0006);
	public static final int VI_ERROR_PARAMETER7 = (int)((-2147483647 - 1) + 0x3FFC0007);
	public static final String DC2100_FIND_PATTERN = (String)"ASRL?*";
	/**
	 * define<br>
	 * Conversion Error : null<br>
	 * SKIPPED:<br>
	 * *
	 */
	public static final int HEAD_WITHOUT_EEPROM = (int)255;
	public static final int STAT_NO_LED1_CHANGED = (int)0x0010;
	public static final int _VI_ERROR = (int)(-2147483647 - 1);
	public static final int VI_INSTR_ERROR_OFFSET = (int)((-2147483647 - 1) + 0x3FFC0900);
	public static final int VI_NULL = (int)(0);
	public static final int STAT_LED_LIMIT1 = (int)0x0200;
	/**
	 * define<br>
	 * Conversion Error : null<br>
	 * SKIPPED:<br>
	 * *
	 */
	public static final int STAT_OTP = (int)0x0008;
	public static final int STAT_OTP_CHANGED = (int)0x0004;
	public static final int DC2100_ERR_DESCR_BUFFER_SIZE = (int)512;
	public static final int MODUS_CONST_CURRENT = (int)0;
	public static final int STAT_VCC_FAIL_CHANGED = (int)0x0001;
	/**
	 * Original signature : <code>_VI_FUNC DC2100_init(ViRsrc, ViBoolean, ViBoolean, ViPSession)</code><br>
	 * <i>native declaration : line 109</i><br>
	 * @deprecated use the safer methods {@link #DC2100_init(java.nio.ByteBuffer, short, short, com.sun.jna.ptr.NativeLongByReference)} and {@link #DC2100_init(com.sun.jna.Pointer, short, short, com.sun.jna.ptr.NativeLongByReference)} instead
	 */
	@Deprecated 
	public static native ThorlabsDC2100Library.__stdcall DC2100_init(Pointer resourceName, short IDQuery, short resetDevice, NativeLongByReference instrumentHandle);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_init(ViRsrc, ViBoolean, ViBoolean, ViPSession)</code><br>
	 * <i>native declaration : line 109</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_init(WString resourceName, int IDQuery, int resetDevice, NativeLongByReference instrumentHandle);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_setLimitCurrent(ViSession, ViReal32)</code><br>
	 * <i>native declaration : line 114</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_setLimitCurrent(NativeLong instrumentHandle, float limit);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getLimitCurrent(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 115</i><br>
	 * @deprecated use the safer methods {@link #DC2100_getLimitCurrent(com.sun.jna.NativeLong, java.nio.FloatBuffer)} and {@link #DC2100_getLimitCurrent(com.sun.jna.NativeLong, com.sun.jna.ptr.FloatByReference)} instead
	 */
	@Deprecated 
	public static native ThorlabsDC2100Library.__stdcall DC2100_getLimitCurrent(NativeLong instrumentHandle, FloatByReference limit);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getLimitCurrent(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 115</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getLimitCurrent(NativeLong instrumentHandle, FloatBuffer limit);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_setMaxLimit(ViSession, ViReal32)</code><br>
	 * <i>native declaration : line 120</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_setMaxLimit(NativeLong instrumentHandle, float limit);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getMaxLimit(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 121</i><br>
	 * @deprecated use the safer methods {@link #DC2100_getMaxLimit(com.sun.jna.NativeLong, java.nio.FloatBuffer)} and {@link #DC2100_getMaxLimit(com.sun.jna.NativeLong, com.sun.jna.ptr.FloatByReference)} instead
	 */
	@Deprecated 
	public static native ThorlabsDC2100Library.__stdcall DC2100_getMaxLimit(NativeLong instrumentHandle, FloatByReference limit);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getMaxLimit(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 121</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getMaxLimit(NativeLong instrumentHandle, FloatBuffer limit);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_setOperationMode(ViSession, ViInt32)</code><br>
	 * <i>native declaration : line 126</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_setOperationMode(NativeLong instrumentHandle, NativeLong operationMode);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getOperationMode(ViSession, ViPInt32)</code><br>
	 * <i>native declaration : line 127</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getOperationMode(NativeLong instrumentHandle, NativeLongByReference operationMode);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_setLedOnOff(ViSession, ViBoolean)</code><br>
	 * <i>native declaration : line 132</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_setLedOnOff(NativeLong instrumentHandle, short LEDOnOff);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getLedOnOff(ViSession, ViPBoolean)</code><br>
	 * <i>native declaration : line 133</i><br>
	 * @deprecated use the safer methods {@link #DC2100_getLedOnOff(com.sun.jna.NativeLong, java.nio.ShortBuffer)} and {@link #DC2100_getLedOnOff(com.sun.jna.NativeLong, com.sun.jna.ptr.ShortByReference)} instead
	 */
	@Deprecated 
	public static native ThorlabsDC2100Library.__stdcall DC2100_getLedOnOff(NativeLong instrumentHandle, ShortByReference LEDOutputState);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getLedOnOff(ViSession, ViPBoolean)</code><br>
	 * <i>native declaration : line 133</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getLedOnOff(NativeLong instrumentHandle, ShortBuffer LEDOutputState);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_setConstCurrent(ViSession, ViReal32)</code><br>
	 * <i>native declaration : line 138</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_setConstCurrent(NativeLong instrumentHandle, float current);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getConstCurrent(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 139</i><br>
	 * @deprecated use the safer methods {@link #DC2100_getConstCurrent(com.sun.jna.NativeLong, java.nio.FloatBuffer)} and {@link #DC2100_getConstCurrent(com.sun.jna.NativeLong, com.sun.jna.ptr.FloatByReference)} instead
	 */
	@Deprecated 
	public static native long DC2100_getConstCurrent(NativeLong instrumentHandle, FloatByReference current);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getConstCurrent(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 139</i>
	 */
	public static native long DC2100_getConstCurrent(NativeLong instrumentHandle, FloatBuffer current);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_setPWMCurrent(ViSession, ViReal32)</code><br>
	 * <i>native declaration : line 144</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_setPWMCurrent(NativeLong instrumentHandle, float current);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getPWMCurrent(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 145</i><br>
	 * @deprecated use the safer methods {@link #DC2100_getPWMCurrent(com.sun.jna.NativeLong, java.nio.FloatBuffer)} and {@link #DC2100_getPWMCurrent(com.sun.jna.NativeLong, com.sun.jna.ptr.FloatByReference)} instead
	 */
	@Deprecated 
	public static native ThorlabsDC2100Library.__stdcall DC2100_getPWMCurrent(NativeLong instrumentHandle, FloatByReference current);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getPWMCurrent(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 145</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getPWMCurrent(NativeLong instrumentHandle, FloatBuffer current);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_setPWMFrequency(ViSession, ViInt32)</code><br>
	 * <i>native declaration : line 150</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_setPWMFrequency(NativeLong instrumentHandle, NativeLong frequency);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getPWMFrequency(ViSession, ViPInt32)</code><br>
	 * <i>native declaration : line 151</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getPWMFrequency(NativeLong instrumentHandle, NativeLongByReference frequency);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_setPWMDutyCycle(ViSession, ViInt32)</code><br>
	 * <i>native declaration : line 156</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_setPWMDutyCycle(NativeLong instrumentHandle, NativeLong dutyCycle);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getPWMDutyCycle(ViSession, ViPInt32)</code><br>
	 * <i>native declaration : line 157</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getPWMDutyCycle(NativeLong instrumentHandle, NativeLongByReference dutyCycle);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_setPWMCounts(ViSession, ViInt32)</code><br>
	 * <i>native declaration : line 162</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_setPWMCounts(NativeLong instrumentHandle, NativeLong counts);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getPWMCounts(ViSession, ViPInt32)</code><br>
	 * <i>native declaration : line 163</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getPWMCounts(NativeLong instrumentHandle, NativeLongByReference counts);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_setDispBright(ViSession, ViInt32)</code><br>
	 * <i>native declaration : line 168</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_setDispBright(NativeLong instrumentHandle, NativeLong displayBrightness);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getDispBright(ViSession, ViPInt32)</code><br>
	 * <i>native declaration : line 169</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getDispBright(NativeLong instrumentHandle, NativeLongByReference displayBrightness);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getStatusRegister(ViSession, ViPInt32)</code><br>
	 * <i>native declaration : line 174</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getStatusRegister(NativeLong instrumentHandle, NativeLongByReference statusRegister);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getWavelength(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 200</i><br>
	 * @deprecated use the safer methods {@link #DC2100_getWavelength(com.sun.jna.NativeLong, java.nio.FloatBuffer)} and {@link #DC2100_getWavelength(com.sun.jna.NativeLong, com.sun.jna.ptr.FloatByReference)} instead
	 */
	@Deprecated 
	public static native ThorlabsDC2100Library.__stdcall DC2100_getWavelength(NativeLong instrumentHandle, FloatByReference wavelength);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getWavelength(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 200</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getWavelength(NativeLong instrumentHandle, FloatBuffer wavelength);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getForwardBias(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 205</i><br>
	 * @deprecated use the safer methods {@link #DC2100_getForwardBias(com.sun.jna.NativeLong, java.nio.FloatBuffer)} and {@link #DC2100_getForwardBias(com.sun.jna.NativeLong, com.sun.jna.ptr.FloatByReference)} instead
	 */
	@Deprecated 
	public static native ThorlabsDC2100Library.__stdcall DC2100_getForwardBias(NativeLong instrumentHandle, FloatByReference forwardBias);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_getForwardBias(ViSession, ViPReal32)</code><br>
	 * <i>native declaration : line 205</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_getForwardBias(NativeLong instrumentHandle, FloatBuffer forwardBias);
	/**
	 * Original signature : <code>_VI_FUNC DC2100_close(ViSession)</code><br>
	 * <i>native declaration : line 210</i>
	 */
	public static native ThorlabsDC2100Library.__stdcall DC2100_close(NativeLong instrumentHandle);
	
	
	public static class __stdcall extends PointerType {
		public __stdcall(Pointer address) {
			super(address);
		}
		public __stdcall() {
			super();
		}
	};
}