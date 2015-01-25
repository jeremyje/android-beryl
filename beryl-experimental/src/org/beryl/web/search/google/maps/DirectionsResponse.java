package org.beryl.web.search.google.maps;

import java.util.ArrayList;

import org.beryl.web.search.AbstractSearchResponseSegment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectionsResponse extends AbstractSearchResponseSegment
{
	public static DirectionsResponse getResponse(String response)
	{
		DirectionsResponse result = new DirectionsResponse();
		result.inflateResponse(response);
		return result;
	}
	
	JSONObject _response;
	
	public String Status;
	public ArrayList<DirectionsRoute> Routes = new ArrayList<DirectionsRoute>();
	@Override
	public void inflateResponse(JSONObject jo) throws JSONException
	{
		_response = jo;
		
		Status = jsonString("status", jo);
		JSONArray array = jo.getJSONArray("routes");
		int i;
		int len = array.length();
		
		for(i = 0; i < len; i++)
		{
			DirectionsRoute route = new DirectionsRoute();
			route.inflateResponse(array.getJSONObject(i));
			Routes.add(route);
		}
	}
}
