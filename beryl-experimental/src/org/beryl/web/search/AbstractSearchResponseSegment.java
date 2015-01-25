package org.beryl.web.search;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractSearchResponseSegment
{
	protected JSONObject _response = null;
	
	
	
	protected AbstractSearchResponseSegment()
	{
		
	}
	
	public abstract void inflateResponse(JSONObject jo) throws JSONException;
	protected boolean inflateResponse(String response)
	{
		boolean result = true;

		try
		{
			JSONObject jo = new JSONObject(response);
			inflateResponse(jo);
		} catch (JSONException e)
		{
			result = false;
		}

		return result;
	}
	
	protected final String jsonString(String key, JSONObject json) throws JSONException
	{
		if(json.has(key))
		{
			return json.getString(key);
		}
		
		return null;
	}
	
	protected final int jsonInt(String key, JSONObject json) throws JSONException
	{
		if(json.has(key))
		{
			return json.getInt(key);
		}
		
		return 0;
	}
	
	protected final JSONObject jsonObject(String key, JSONObject json) throws JSONException
	{
		if(json.has(key))
		{
			return json.getJSONObject(key);
		}
		
		return null;
	}
	
	protected final JSONArray jsonArray(String key, JSONObject json) throws JSONException
	{
		if(json.has(key))
		{
			return json.getJSONArray(key);
		}
		return null;
	}
	
	protected final double jsonDouble(String key, JSONObject json) throws JSONException
	{
		if(json.has(key))
		{
			return json.getDouble(key);
		}
		
		return 0.0;
	}
	
	public JSONObject getResponseJSONObject()
	{
		return _response;
	}
	
	@Override
	public String toString()
	{
		return _response.toString();
	}
	
	
	protected final Date jsonDate(String key, JSONObject json) throws JSONException
	{
		if(json.has(key))
		{
			return parseDate(json.getString(key));
		}
		return null;
	}
	
	private static final SimpleDateFormat _bingDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private static Date parseDate(String date_string)
	{
		Date result = null;
		try
		{
			result = _bingDateFormat.parse(date_string);
		}
		catch (ParseException e)
		{
		}
		
		return result;
	}
}
