package org.beryl.util;

import org.json.JSONException;
import org.json.JSONObject;

public class TryParse {

	public static int toInt(String value, int defaultValue) {
		int result;

		try {
			result = Integer.parseInt(value);
		} catch(NumberFormatException e) { result = defaultValue; }

		return result;
	}

	public static String toString(JSONObject json, String name, String defaultValue) {
		String result;
		try {
			result = json.getString(name);
		} catch(JSONException e) { result = defaultValue; }

		return result;
	}

	public static int toInt(JSONObject json, String name, int defaultValue) {
		int result;
		try {
			result = json.getInt(name);
		} catch(JSONException e) { result = defaultValue; }

		return result;
	}

	public static boolean toBoolean(JSONObject json, String name, boolean defaultValue) {
		boolean result;
		try {
			result = json.getBoolean(name);
		} catch(JSONException e) { result = defaultValue; }

		return result;
	}
}
