package org.beryl.location;

import java.util.ArrayList;

import android.location.LocationListener;

/** Base class for controllers that can be attached to a LocationMonitor.
 * These objects can modify the state of the LocationMonitor based on its state and are generally used to delegate some management task.
 * For an example look at {@link org.beryl.location.PreferGpsLocationMonitorController} which attempts to use GPS but for responsiveness uses lesser providers until GPS is available.
 * Controllers have the ability to interrupt incoming location readings and toggle providers as they see fit. */
public abstract class LocationMonitorController implements LocationListener {

	protected LocationMonitor Monitor;

	public LocationMonitorController(LocationMonitor monitor) {
		setMonitor(monitor);
	}

	void setMonitor(LocationMonitor monitor) {
		Monitor = monitor;
	}

	protected ArrayList<String> getListeningProviders() {
		return Monitor.getListeningProviders();
	}
}
