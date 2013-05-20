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
		if (isInitialized()) {
			int result = ThorlabsDC2100Library.DC2100_close(sessionHandle.getValue());
			return isDeviceOk(result);
		}
		return false;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public boolean isLedOn() {
		if (isInitialized()) {
			ShortByReference result = new ShortByReference();
			ThorlabsDC2100Library.DC2100_getLedOnOff(sessionHandle.getValue(), result);
			return ThorlabsDC2100Library.VI_ON == Short.valueOf(result.getValue()).intValue();
		}
		return false;
	}
	
	public boolean setLedOn(boolean on) {
		if (isInitialized()) {
			int ledState = on ? ThorlabsDC2100Library.VI_ON : ThorlabsDC2100Library.VI_OFF;
			int result = ThorlabsDC2100Library.DC2100_setLedOnOff(sessionHandle.getValue(), ledState);
			return isDeviceOk(result);
		}
		return false;
	}
	
	public int getPwmFrequency() {
		if (isInitialized()) {
			NativeLongByReference result = new NativeLongByReference();
			ThorlabsDC2100Library.DC2100_getPWMFrequency(sessionHandle.getValue(), result);
			return result.getValue().intValue();
		}
		return -1;
	}
	
	public boolean setPwmFrequency(int frequency) {
		if (isInitialized()) {
			NativeLong pwmFrequency = new NativeLong(Integer.valueOf(frequency).longValue());
			int result = ThorlabsDC2100Library.DC2100_setPWMFrequency(sessionHandle.getValue(), pwmFrequency);
			return isDeviceOk(result);
		}
		return false;
	}
	
	public int getPwmCurrent() {
		if (isInitialized()) {
			FloatByReference result = new FloatByReference();
			ThorlabsDC2100Library.DC2100_getPWMCurrent(sessionHandle.getValue(), result);
			return Float.valueOf(result.getValue()).intValue();
		}
		return -1;
	}
	
	public boolean setPwmCurrent(int current) {
		if (isInitialized()) {
			float pwmCurrent = Integer.valueOf(current).floatValue();
			int result = ThorlabsDC2100Library.DC2100_setPWMCurrent(sessionHandle.getValue(), pwmCurrent);
			return isDeviceOk(result);
		}
		return false;
	}
	
	public int getDutyCycle() {
		if (isInitialized()) {
			NativeLongByReference result = new NativeLongByReference();
			ThorlabsDC2100Library.DC2100_getPWMDutyCycle(sessionHandle.getValue(), result);
			return result.getValue().intValue();
		}
		return -1;
	}
	
	public boolean setDutyCycle(int dutyCycle) {
		if (isInitialized()) {
			NativeLong pwmDutyCycle = new NativeLong(Integer.valueOf(dutyCycle).longValue());
			int result = ThorlabsDC2100Library.DC2100_setPWMDutyCycle(sessionHandle.getValue(), pwmDutyCycle);
			return isDeviceOk(result);
		}
		return false;
	}
	
	public int getPwmCounts() {
		if (isInitialized()) {
			NativeLongByReference result = new NativeLongByReference();
			ThorlabsDC2100Library.DC2100_getPWMCounts(sessionHandle.getValue(), result);
			return result.getValue().intValue();
		}
		return -1;
	}
	
	public boolean setPwmCounts(int counts) {
		if (isInitialized()) {
			NativeLong pwmCounts = new NativeLong(counts);
			int result = ThorlabsDC2100Library.DC2100_setPWMCounts(sessionHandle.getValue(), pwmCounts);
			return isDeviceOk(result);
		}
		return false;
	}
	
	public boolean setConstantCurrent(int current) {
		if (isInitialized()) {
			float constantCurrent = Integer.valueOf(current).floatValue();
			int result = ThorlabsDC2100Library.DC2100_setConstCurrent(sessionHandle.getValue(), constantCurrent);
			return isDeviceOk(result);
		}
		return false;
	}

	public int getConstantCurrent() {
		if (isInitialized()) {
			FloatByReference result = new FloatByReference();
			ThorlabsDC2100Library.DC2100_getConstCurrent(sessionHandle.getValue(), result);
			return Float.valueOf(result.getValue()).intValue();
		}
		return -1;
	}
	
	public int getCurrentLimit() {
		if (isInitialized()) {
			FloatByReference result = new FloatByReference();
			ThorlabsDC2100Library.DC2100_getLimitCurrent(sessionHandle.getValue(), result);
			return Float.valueOf(result.getValue()).intValue();
		}
		return -1;
	}
	
	public boolean setCurrentLimit(int current) {
		if (isInitialized()) {
			float currentLimit = Integer.valueOf(current).floatValue();
			int result = ThorlabsDC2100Library.DC2100_setLimitCurrent(sessionHandle.getValue(), currentLimit);
			return isDeviceOk(result);
		}
		return false;
	}
	
	public OperationMode getOperationMode() {
		if (isInitialized()) {
			NativeLongByReference result = new NativeLongByReference();
			ThorlabsDC2100Library.DC2100_getOperationMode(sessionHandle.getValue(), result);
			return OperationMode.findOperationMode(result.getValue().intValue());
		}
		return OperationMode.NOT_INITIALIZED;
	}
	
	public boolean setOperationMode(OperationMode mode) {
		if (isInitialized()) {
			boolean result = setLedOn(false);
			NativeLong operationMode = new NativeLong(mode.ordinal());
			int operationModeResult = ThorlabsDC2100Library.DC2100_setOperationMode(sessionHandle.getValue(), operationMode);
			return result && isDeviceOk(operationModeResult);
		}
		return false;
	}

	public DriverStatus getStatus() {
		if (isInitialized()) {
			NativeLongByReference result = new NativeLongByReference();
			ThorlabsDC2100Library.DC2100_getStatusRegister(sessionHandle.getValue(), result);
			return DriverStatus.findDriverStatus(result.getValue().intValue());
		}
		return DriverStatus.NO_LED;
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
		
		CONSTANT_CURRENT, PWM, EXTERNAL_CONTROL, NOT_INITIALIZED;
		
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
