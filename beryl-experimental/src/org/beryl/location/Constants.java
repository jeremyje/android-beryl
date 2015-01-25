package org.beryl.location;

/** All the constants used internally for this package. */
public class Constants {

	/** 100 meters */
	public static final float DEFAULT_INTERVAL_DISTANCE = 100.0f;

	/** 1 minute */
	public static final long ONE_MINUTE_IN_MILLIS = 60 * 1000;

	/** 1 minute */
	public static final long DEFAULT_INTERVAL_TIME = ONE_MINUTE_IN_MILLIS;

	/** Street Accuracy */
	public static final int ACCURACY_STREETLEVEL = 8; // in meters.

	/** City Level Accuracy */
	public static final int ACCURACY_CITYLEVEL = 150; // in meters.

	/** Reasonable accuracy for most location aware activities. */
	public static final int ACCURACY_REASONABLE = ACCURACY_CITYLEVEL;
}
