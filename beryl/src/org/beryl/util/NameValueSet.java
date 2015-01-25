package org.beryl.util;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

/** Represents a set of name-value pairs that can be flattened into a JSON formatted string.
 */
public class NameValueSet extends HashMap<String,String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5224637620557552416L;

	/** Converts the name-value pair set into a JSON string. */
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		for(String key : keySet()) {
			try {
				json.put(key, get(key));
			} catch (JSONException e) {
			}
		}
		return json.toString();
	}
	
	/** Appends the data from the JSON formatted string into the name-value pair set. */
	public void fromString(String values) {
		try {
			JSONObject json = new JSONObject(values);
			Iterator<?> keySet = json.keys();
			while(keySet.hasNext()) {
				String key = (String)keySet.next();
				this.put(key, json.getString(key));
			}
		} catch (JSONException e) {
		}
	}
	
	/** Creates a name-value pair set from a JSON formatted string. */
	public static NameValueSet fromJsonString(String values) {
		final NameValueSet result = new NameValueSet();
		result.fromString(values);
		return result;
	}
}
