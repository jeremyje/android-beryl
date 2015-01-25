package org.beryl.diagnostics;

import org.beryl.app.AndroidVersion;

import android.os.StrictMode;

/** Manages the enabling of Strict Mode which will record to a penalty log.
 * Note: Strict Mode is only supported in Gingerbread or higher.
 * If methods are called on an unsupported system they will silently be ignored. */
public class StrictModeEnabler {

	/** Enables strict mode on the current thread. Note: Strict Mode is only supported in Gingerbread or higher. */
	public static void enableOnThread() {
		IStrictModeEnabler enabler = getStrictModeEnabler();
		enabler.startStrictMode();
	}

	private static IStrictModeEnabler getStrictModeEnabler() {
		if(AndroidVersion.isGingerbreadOrHigher()) {
			return new GingerbreadAndAboveStrictModeEnabler();
		} else {
			return new NoStrictModeEnabler();
		}
	}

	static interface IStrictModeEnabler {
		void startStrictMode();
	}

	static class GingerbreadAndAboveStrictModeEnabler implements IStrictModeEnabler {
		public void startStrictMode() {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
		}
	}
	static class NoStrictModeEnabler implements IStrictModeEnabler {

		public void startStrictMode() {
			// Strict Mode Not supported.
		}
	}
}
