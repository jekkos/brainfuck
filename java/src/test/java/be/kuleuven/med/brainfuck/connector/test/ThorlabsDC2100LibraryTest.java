package be.kuleuven.med.brainfuck.connector.test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Connector;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Connector.OperationMode;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Library;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.ShortByReference;

public class ThorlabsDC2100LibraryTest extends TestCase {

	public void testLibrary() throws UnsupportedEncodingException {
		NativeLongByReference sessionHandle = new NativeLongByReference(new NativeLong(ThorlabsDC2100Library.VI_NULL));
		ByteBuffer encode = Charset.forName(ThorlabsDC2100Library.STRING_ENCODING).encode("ASRL4::INSTR");
		int initResult = ThorlabsDC2100Library.DC2100_init(encode, ThorlabsDC2100Library.VI_FALSE, ThorlabsDC2100Library.VI_TRUE, sessionHandle.getPointer());
		assertEquals(ThorlabsDC2100Library.VI_SUCCESS, initResult);
		System.out.println("Initialisation " + isSuccess(initResult));
		
		int setLedOnOffResult = ThorlabsDC2100Library.DC2100_setLedOnOff(sessionHandle.getValue(), ThorlabsDC2100Library.VI_OFF);
		assertEquals(ThorlabsDC2100Library.VI_SUCCESS, setLedOnOffResult);
		System.out.println("Led set on/off " + isSuccess(setLedOnOffResult));
		ShortByReference ledOnOff = new ShortByReference(); 
		int getLedOnOffResult = ThorlabsDC2100Library.DC2100_getLedOnOff(sessionHandle.getValue(), ledOnOff);
		int ledOnOffResult = Short.valueOf(ledOnOff.getValue()).intValue();
		assertEquals(0, ledOnOffResult);
		System.out.println("Led get on/off " + isSuccess(getLedOnOffResult) + ": " + ledOnOffResult);
		
		// set in constant current mode
		OperationMode constantCurrentMode = ThorlabsDC2100Connector.OperationMode.CONSTANT_CURRENT;
		int setConstantCurrentMode = ThorlabsDC2100Library.DC2100_setOperationMode(sessionHandle.getValue(), new NativeLong(constantCurrentMode.ordinal()));
		if (ThorlabsDC2100Library.VI_SUCCESS == setConstantCurrentMode) {
			int setCurrentResult = ThorlabsDC2100Library.DC2100_setConstCurrent(sessionHandle.getValue(), 20f);
			assertEquals(ThorlabsDC2100Library.VI_SUCCESS, setCurrentResult);
			System.out.println("Current set " + isSuccess(setCurrentResult));
			FloatByReference constantCurrent = new FloatByReference();
			int getCurrentResult = ThorlabsDC2100Library.DC2100_getConstCurrent(sessionHandle.getValue(), constantCurrent);
			assertEquals(ThorlabsDC2100Library.VI_SUCCESS, getCurrentResult);
			float constantCurrentResult = constantCurrent.getValue();
			assertEquals(20f, constantCurrentResult);
			System.out.println("Current get " + isSuccess(getCurrentResult) + ": " + constantCurrentResult + "A");
		}
		
		// set in pwm mode
		OperationMode pwmCurrentMode = ThorlabsDC2100Connector.OperationMode.PWM;
		int setPwmMode = ThorlabsDC2100Library.DC2100_setOperationMode(sessionHandle.getValue(), new NativeLong(pwmCurrentMode.ordinal()));
		if (ThorlabsDC2100Library.VI_SUCCESS == setPwmMode) {
			int setFlickerFrequencyResult = ThorlabsDC2100Library.DC2100_setPWMFrequency(sessionHandle.getValue(), new NativeLong(5L));
			assertEquals(ThorlabsDC2100Library.VI_SUCCESS, setFlickerFrequencyResult);
			System.out.println("Flicker frequency set " + isSuccess(setFlickerFrequencyResult));
			NativeLongByReference frequency = new NativeLongByReference();
			int getFlickerFrequencyResult = ThorlabsDC2100Library.DC2100_getPWMFrequency(sessionHandle.getValue(), frequency);
			int pwmFrequencyResult = frequency.getValue().intValue();
			assertEquals(5, pwmFrequencyResult);
			System.out.println("Flicker frequency get " + isSuccess(getFlickerFrequencyResult) + ": " + pwmFrequencyResult + "Hz");
			int setPWMCurrentResult = ThorlabsDC2100Library.DC2100_setPWMCurrent(sessionHandle.getValue(), 100f);
			assertEquals(ThorlabsDC2100Library.VI_SUCCESS, setPWMCurrentResult);
			System.out.println("PWM current set " + isSuccess(setPWMCurrentResult));
			FloatByReference pwmCurrent = new FloatByReference();
			int getPWMCurrentResult = ThorlabsDC2100Library.DC2100_getPWMCurrent(sessionHandle.getValue(), pwmCurrent);
			assertEquals(ThorlabsDC2100Library.VI_SUCCESS, getPWMCurrentResult);
			float pwmCurrentResult = pwmCurrent.getValue();
			assertEquals(100f, pwmCurrentResult);
			System.out.println("PWM current get " + pwmCurrent + isSuccess(getPWMCurrentResult) + ": " + pwmCurrentResult + "A");
		}
		
		setLedOnOffResult = ThorlabsDC2100Library.DC2100_setLedOnOff(sessionHandle.getValue(), ThorlabsDC2100Library.VI_ON);
		assertEquals(ThorlabsDC2100Library.VI_SUCCESS, setLedOnOffResult);
		System.out.println("Led set on/off " + isSuccess(setLedOnOffResult));
		getLedOnOffResult = ThorlabsDC2100Library.DC2100_getLedOnOff(sessionHandle.getValue(), ledOnOff);
		ledOnOffResult = Short.valueOf(ledOnOff.getValue()).intValue();
		assertEquals(ThorlabsDC2100Library.VI_ON, ledOnOffResult);
		System.out.println("Led get on/off " + isSuccess(getLedOnOffResult) + ": " + ledOnOffResult);
		
		int closingResult = ThorlabsDC2100Library.DC2100_close(sessionHandle.getValue());
		assertEquals(ThorlabsDC2100Library.VI_SUCCESS, closingResult);
		System.out.println("Closing " + isSuccess(closingResult));
	}
	
	private String isSuccess(int resultCode) {
		return resultCode == ThorlabsDC2100Library.VI_SUCCESS ? "succeeded" : "failed (" + resultCode + ")";
	}
	
}
