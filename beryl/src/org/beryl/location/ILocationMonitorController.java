package org.beryl.location;

import android.location.LocationListener;

/** Describes LocationListeners that are designed to control LocationMonitor.
 * 
 * GC Note: Do not hold a reference to this object once it binds to a LocationMonitor.
*/
public interface ILocationMonitorController extends LocationListener {
	
	/** Called when this controller is bound to a LocationMonitor.
	 * Hold this object to control the monitor. */
	void obtainController(LocationMonitor.Controller controller);
}
