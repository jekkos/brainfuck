package be.kuleuven.med.brainfuck.connector.test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Library;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;

public class ThorlabsLibraryTest extends TestCase {

	public void testLibrary() throws UnsupportedEncodingException {
		NativeLongByReference sessionHandle = new NativeLongByReference(new NativeLong(ThorlabsDC2100Library.VI_NULL));
		ThorlabsDC2100Library.DC2100_close(sessionHandle.getPointer());
		ByteBuffer encode = Charset.forName(ThorlabsDC2100Library.STRING_ENCODING).encode("ASRL4::INSTR");
		int result = ThorlabsDC2100Library.DC2100_init(encode, ThorlabsDC2100Library.VI_FALSE, ThorlabsDC2100Library.VI_TRUE, sessionHandle.getPointer());
		int lastError = Native.getLastError();
		/*FloatBuffer floatBuffer = FloatBuffer.allocate(16);
		floatBuffer.put(200f);
		NativeLong dc2100_getConstCurrent = ThorlabsDC2100Library.DC2100_getConstCurrent(handle.getValue(), floatBuffer);
		System.out.println(dc2100_getConstCurrent.longValue());
		long currentResult = ThorlabsDC2100Library.DC2100_setLimitCurrent(handle.getValue(), 40f);
		ThorlabsDC2100Library.DC2100_setLedOnOff(handle.getValue(), (short) 1);*/
		//ThorlabsDC2100Library.DC2100_setLedOnOff(handle.getValue(), (short) 1);
		System.out.println("Init" + result);
		
		ThorlabsDC2100Library.DC2100_close(sessionHandle.getPointer());
	}
	
}
