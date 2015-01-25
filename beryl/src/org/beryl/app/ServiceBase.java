package org.beryl.app;

import org.beryl.diagnostics.Log;
import org.beryl.diagnostics.Logger;

/**
 * Service base class that includes various utility methods that are commonly used in services.
 */
public abstract class ServiceBase extends ServiceCompat {

	protected final Log log = Logger.newInstance(getTag());
	
	@Override
	public void onCreate() {
		super.onCreate();
		Thread.currentThread().setName(getTag());
	}

	/** Tag used for logging and service identification. */
	protected abstract String getTag();
}
