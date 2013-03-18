package be.kuleuven.med.brainfuck.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import be.kuleuven.med.brainfuck.entity.LedMatrix;

public class LedMatrixModel {
	
	public static final String HEIGHT = "height";

	public static final String WIDTH = "width";

	public static final String SERIAL_PORT_NAMES = "serialPortNames";
	
	public static final String SELECTED_SERIAL_PORT_NAME = "selectedSerialPort";
	
	private LedMatrix ledMatrix;
		
	private List<String> serialPortNames;
	
	private String selectedSerialPortName;

	private transient final java.beans.PropertyChangeSupport pcs = new java.beans.PropertyChangeSupport(this);
	
	public LedMatrixModel(LedMatrix ledMatrix) {
		this.ledMatrix = ledMatrix;
	}
	
    public int getWidth() {
		return ledMatrix.getWidth();
	}

	public void setWidth(int width) {
		firePropertyChange(WIDTH, ledMatrix.getWidth(), width);
		ledMatrix.setWidth(width);
	}

	public int getHeight() {
		return ledMatrix.getHeight();
	}

	public void setHeight(int height) {
		firePropertyChange(HEIGHT, ledMatrix.getHeight(), height);
		ledMatrix.setHeight(height);
	}

	public List<String> getSerialPortNames() {
		return serialPortNames;
	}

	public void setSerialPortNames(List<String> serialPortNames) {
		firePropertyChange(SERIAL_PORT_NAMES, this.serialPortNames, this.serialPortNames = serialPortNames);
	}
	
	public String getSelectedSerialPortName() {
		return selectedSerialPortName;
	}

	public void setSelectedSerialPortName(String selectedSerialPortName) {
		firePropertyChange(SELECTED_SERIAL_PORT_NAME, this.selectedSerialPortName, this.selectedSerialPortName = selectedSerialPortName);
	}

	/**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties and its 
     * {@code propertyChange} method will run on the event dispatching
     * thread.
     * <p>
     * If {@code listener} is null, no exception is thrown and no action
     * is taken.
     *
     * @param listener the PropertyChangeListener to be added.
     * @see #removePropertyChangeListener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * <p>
     * If {@code listener} is null, no exception is thrown and no action
     * is taken.
     *
     * @param listener the PropertyChangeListener to be removed.
     * @see #addPropertyChangeListener
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Add a PropertyChangeListener for a specific property.  The listener
     * will be invoked only when a call on firePropertyChange names that
     * specific property.
     * The same listener object may be added more than once.  For each
     * property,  the listener will be invoked the number of times it was added
     * for that property.
     * If <code>propertyName</code> or <code>listener</code> is null, no
     * exception is thrown and no action is taken.
     *
     * @param propertyName  The name of the property to listen on.
     * @param listener  the PropertyChangeListener to be added
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(String, PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Remove a PropertyChangeListener for a specific property.
     * If <code>listener</code> was added more than once to the same event
     * source for the specified property, it will be notified one less time
     * after being removed.
     * If <code>propertyName</code> is null,  no exception is thrown and no
     * action is taken.
     * If <code>listener</code> is null, or was never added for the specified
     * property, no exception is thrown and no action is taken.
     *
     * @param propertyName  The name of the property that was listened on.
     * @param listener  The PropertyChangeListener to be removed
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(String, PropertyChangeListener)
     */
    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * An array of all of the {@code PropertyChangeListeners} added so far.
     * 
     * @return all of the {@code PropertyChangeListeners} added so far.
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners();
    }

    /**
     * Called whenever the value of a bound property is set.
     * <p>
     * If oldValue is not equal to newValue, invoke the {@code
     * propertyChange} method on all of the {@code
     * PropertyChangeListeners} added so far, on the event
     * dispatching thread.
     * 
     * @see #addPropertyChangeListener
     * @see #removePropertyChangeListener
     * @see java.beans.PropertyChangeSupport#firePropertyChange(String, Object, Object)
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            return;
        }
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Fire an existing PropertyChangeEvent 
     * <p>
     * If the event's oldValue property is not equal to newValue, 
     * invoke the {@code propertyChange} method on all of the {@code
     * PropertyChangeListeners} added so far, on the event
     * dispatching thread.
     * 
     * @see #addPropertyChangeListener
     * @see #removePropertyChangeListener
     * @see java.beans.PropertyChangeSupport#firePropertyChange(PropertyChangeEvent e)
     */
    public void firePropertyChange(PropertyChangeEvent e) {
        pcs.firePropertyChange(e);
    }

}
