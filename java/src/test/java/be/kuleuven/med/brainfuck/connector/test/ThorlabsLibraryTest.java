package be.kuleuven.med.brainfuck.connector.test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Library;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.ShortByReference;

public class ThorlabsLibraryTest extends TestCase {

	public void testLibrary() throws UnsupportedEncodingException {
		NativeLongByReference sessionHandle = new NativeLongByReference(new NativeLong(ThorlabsDC2100Library.VI_NULL));
		ByteBuffer encode = Charset.forName(ThorlabsDC2100Library.STRING_ENCODING).encode("ASRL4::INSTR");
		int result = ThorlabsDC2100Library.DC2100_init(encode, ThorlabsDC2100Library.VI_FALSE, ThorlabsDC2100Library.VI_TRUE, sessionHandle.getPointer());
		assertEquals(ThorlabsDC2100Library.VI_SUCCESS, result);
		System.out.println("Initialisation " + isSuccess(result));
		FloatByReference constantCurrent = new FloatByReference();
		int getCurrentResult = ThorlabsDC2100Library.DC2100_getConstCurrent(sessionHandle.getValue(), constantCurrent);
		assertEquals(ThorlabsDC2100Library.VI_SUCCESS, getCurrentResult);
		System.out.println("Current get " + isSuccess(getCurrentResult) + ": " + constantCurrent.getValue());
		int setCurrentResult = ThorlabsDC2100Library.DC2100_setConstCurrent(sessionHandle.getValue(), 40f);
		System.out.println("Current set " + isSuccess(setCurrentResult));
		int setFlickerFrequency = ThorlabsDC2100Library.DC2100_setPWMFrequency(sessionHandle.getValue(), new NativeLong(5L));
		assertEquals(ThorlabsDC2100Library.VI_SUCCESS, setFlickerFrequency);
		System.out.println("Flicker frequency set " + isSuccess(setFlickerFrequency));
		NativeLongByReference frequency = new NativeLongByReference();
		ThorlabsDC2100Library.DC2100_getPWMFrequency(sessionHandle.getValue(), frequency);
		assertEquals(5, frequency.getValue().intValue());
		ThorlabsDC2100Library.DC2100_setLedOnOff(sessionHandle.getValue(), ThorlabsDC2100Library.VI_ON);
		
		ShortByReference ledOnOff = new ShortByReference(); 
		ThorlabsDC2100Library.DC2100_getLedOnOff(sessionHandle.getValue(), ledOnOff);
		assertEquals(1, Short.valueOf(ledOnOff.getValue()).intValue());
		
		//ThorlabsDC2100Library.DC2100_setLedOnOff(handle.getValue(), (short) 1);
		int closingResult = ThorlabsDC2100Library.DC2100_close(sessionHandle.getValue());
		System.out.println("Closing " + isSuccess(closingResult));
	}
	
	private String isSuccess(int resultCode) {
		return resultCode == ThorlabsDC2100Library.VI_SUCCESS ? "succeeded" : "failed (" + resultCode + ")";
	}
	
}
