package be.kuleuven.med.brainfuck.connector;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import be.kuleuven.med.brainfuck.domain.settings.SerialPortSettings;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.ShortByReference;

public class ThorlabsDC2100Connector extends SerialPortConnector {
	
	private static final String VISA_RESOURCE = "ASRL%1$d::INSTR";

	private NativeLongByReference sessionHandle;
	
	private boolean initialized;

	public ThorlabsDC2100Connector(SerialPortSettings serialPortSettings) {
		super(serialPortSettings);
	}

	public void initialize(String serialPortName) {
		sessionHandle = new NativeLongByReference(new NativeLong(ThorlabsDC2100Library.VI_NULL));
		String lastChar = serialPortName.substring(serialPortName.length() - 1);
		String viResource = String.format(VISA_RESOURCE, Integer.parseInt(lastChar));
		ByteBuffer encode = Charset.forName(ThorlabsDC2100Library.STRING_ENCODING).encode(viResource);
		int initResult = ThorlabsDC2100Library.DC2100_init(encode, ThorlabsDC2100Library.VI_FALSE, ThorlabsDC2100Library.VI_TRUE, sessionHandle.getPointer());
		if (isSuccess(initResult)) {
			initialized = true;
			LOGGER.info("Thorlabs DC2XXX driver initialized successfully");
		} else {
			throw new IllegalStateException("Failed to initialize Thorlabs driver " +  getLastError());
		}
	}
	
	public boolean close() {
		int result = ThorlabsDC2100Library.DC2100_close(sessionHandle.getValue());
		return isDeviceOk(result);
	}

	public boolean isInitialized() {
		return initialized;
	}

	public boolean isLedOn() {
		ShortByReference result = new ShortByReference();
		ThorlabsDC2100Library.DC2100_getLedOnOff(sessionHandle.getValue(), result);
		return ThorlabsDC2100Library.VI_ON == Short.valueOf(result.getValue()).intValue();
	}
	
	public boolean setLedOn(boolean on) {
		int ledState = on ? ThorlabsDC2100Library.VI_ON : ThorlabsDC2100Library.VI_OFF;
		int result = ThorlabsDC2100Library.DC2100_setLedOnOff(sessionHandle.getValue(), ledState);
		return isDeviceOk(result);
	}
	
	public int getPwmFrequency() {
		NativeLongByReference result = new NativeLongByReference();
		ThorlabsDC2100Library.DC2100_getPWMFrequency(sessionHandle.getValue(), result);
		return result.getValue().intValue();
	}
	
	public boolean setPwmFrequency(int frequency) {
		NativeLong pwmFrequency = new NativeLong(Integer.valueOf(frequency).longValue());
		int result = ThorlabsDC2100Library.DC2100_setPWMFrequency(sessionHandle.getValue(), pwmFrequency);
		return isDeviceOk(result);
	}
	
	public int getPwmCurrent() {
		FloatByReference result = new FloatByReference();
		ThorlabsDC2100Library.DC2100_getPWMCurrent(sessionHandle.getValue(), result);
		return Float.valueOf(result.getValue()).intValue();
	}
	
	public boolean setPwmCurrent(int current) {
		float pwmCurrent = Integer.valueOf(current).floatValue();
		int result = ThorlabsDC2100Library.DC2100_setPWMCurrent(sessionHandle.getValue(), pwmCurrent);
		return isDeviceOk(result);
	}
	
	public int getDutyCycle() {
		NativeLongByReference result = new NativeLongByReference();
		ThorlabsDC2100Library.DC2100_getPWMDutyCycle(sessionHandle.getValue(), result);
		return result.getValue().intValue();
	}
	
	public boolean setDutyCycle(int dutyCycle) {
		NativeLong pwmDutyCycle = new NativeLong(Integer.valueOf(dutyCycle).longValue());
		int result = ThorlabsDC2100Library.DC2100_setPWMDutyCycle(sessionHandle.getValue(), pwmDutyCycle);
		return isDeviceOk(result);
	}
	
	public int getPwmCounts() {
		NativeLongByReference result = new NativeLongByReference();
		ThorlabsDC2100Library.DC2100_getPWMCounts(sessionHandle.getValue(), result);
		return result.getValue().intValue();
	}
	
	public boolean setPwmCounts(int counts) {
		NativeLong pwmCounts = new NativeLong(counts);
		int result = ThorlabsDC2100Library.DC2100_setPWMCounts(sessionHandle.getValue(), pwmCounts);
		return isDeviceOk(result);
	}
	
	public boolean setConstantCurrent(int current) {
		float constantCurrent = Integer.valueOf(current).floatValue();
		int result = ThorlabsDC2100Library.DC2100_setConstCurrent(sessionHandle.getValue(), constantCurrent);
		return isDeviceOk(result);
	}

	public int getConstantCurrent() {
		FloatByReference result = new FloatByReference();
		ThorlabsDC2100Library.DC2100_getConstCurrent(sessionHandle.getValue(), result);
		return Float.valueOf(result.getValue()).intValue();
	}
	
	public int getCurrentLimit() {
		FloatByReference result = new FloatByReference();
		ThorlabsDC2100Library.DC2100_getLimitCurrent(sessionHandle.getValue(), result);
		return Float.valueOf(result.getValue()).intValue();
	}
	
	public boolean setCurrentLimit(int current) {
		float currentLimit = Integer.valueOf(current).floatValue();
		int result = ThorlabsDC2100Library.DC2100_setLimitCurrent(sessionHandle.getValue(), currentLimit);
		return isDeviceOk(result);
	}
	
	public OperationMode getOperationMode() {
		NativeLongByReference result = new NativeLongByReference();
		ThorlabsDC2100Library.DC2100_getOperationMode(sessionHandle.getValue(), result);
		return OperationMode.findOperationMode(result.getValue().intValue());
	}
	
	public boolean setOperationMode(OperationMode mode) {
		boolean result = setLedOn(false);
		NativeLong operationMode = new NativeLong(mode.ordinal());
		int operationModeResult = ThorlabsDC2100Library.DC2100_setOperationMode(sessionHandle.getValue(), operationMode);
		return result && isDeviceOk(operationModeResult);
	}

	public DriverStatus getStatus() {
		NativeLongByReference result = new NativeLongByReference();
		ThorlabsDC2100Library.DC2100_getStatusRegister(sessionHandle.getValue(), result);
		return DriverStatus.findDriverStatus(result.getValue().intValue());
	}
	
	private boolean isDeviceOk(int result) {
		return isInitialized() && ThorlabsDC2100Library.VI_SUCCESS == result;
	}
	
	private boolean isSuccess(int result) {
		return ThorlabsDC2100Library.VI_SUCCESS == result;
	}
	
	public String getLastError() {
		return Integer.toString(Native.getLastError());
	}

	public enum DriverStatus {
		NO_LED(ThorlabsDC2100Library.STAT_NO_LED1), 
		LED_OPEN(ThorlabsDC2100Library.STAT_LED_OPEN1), 
		LIMIT(ThorlabsDC2100Library.STAT_LED_LIMIT1), 
		NO_FAULT(0x00);
		
		private int value;
		
		private DriverStatus(int value) {
			this.value = value;
		}
		
		public static DriverStatus findDriverStatus(int status) {
			for (DriverStatus driverStatus : values()) {
				if (status == driverStatus.value) {
					return driverStatus;
				}
			}
			return null;
		}
		
	}
	
	public enum OperationMode {
		
		CONSTANT_CURRENT, PWM, EXTERNAL_CONTROL;
		
		public static OperationMode findOperationMode(int mode) {
			for (OperationMode operationMode : values()) {
				if (operationMode.ordinal() == mode) {
					return operationMode;
				}
			}
			return null;
		}
		
	}

}
