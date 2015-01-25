package org.beryl.util;

public class StringUtils {

	private static final String NullString = "[NULL]";

	/** Converts an object to a string via it's toString() method. If the object itself is null then "[NULL]" is returned. */
	public static String objectToStringNoNull(Object obj) {
		String msg = NullString;
		if (obj != null) msg = obj.toString();
		return msg;
	}
}
