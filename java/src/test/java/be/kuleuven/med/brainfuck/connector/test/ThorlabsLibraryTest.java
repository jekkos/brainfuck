package be.kuleuven.med.brainfuck.connector.test;

import java.nio.FloatBuffer;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Library;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Library.__stdcall;

import com.sun.jna.WString;
import com.sun.jna.ptr.NativeLongByReference;

public class ThorlabsLibraryTest extends TestCase {

	public void testLibrary() {
		NativeLongByReference handle = new NativeLongByReference();
		__stdcall result = ThorlabsDC2100Library.DC2100_init(new WString("ASRL4::INSTR"), 0, 0, handle);
		FloatBuffer floatBuffer = FloatBuffer.allocate(16);
		floatBuffer.put(200f);
		long dc2100_getConstCurrent = ThorlabsDC2100Library.DC2100_getConstCurrent(handle.getValue(), floatBuffer);
		System.out.println(dc2100_getConstCurrent);
		System.out.println(result != null ? result.toString() : "null");
	}
	
}
