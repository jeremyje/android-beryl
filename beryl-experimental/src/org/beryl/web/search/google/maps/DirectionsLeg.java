package org.beryl.web.search.google.maps;

import java.util.ArrayList;

import org.beryl.web.search.AbstractSearchResponseSegment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectionsLeg extends AbstractSearchResponseSegment
{
	public long Distance;
	public String DistanceText;
	public long Duration;
	public String DurationText;
	
	public double StartLatitude;
	public double StartLongitude;
	public double EndLatitude;
	public double EndLongitude;
	
	public String StartAddress;
	public String EndAddress;
	
	public ArrayList<DirectionsSteps> Steps = new ArrayList<DirectionsSteps>();
	
	@Override
	public void inflateResponse(JSONObject jo) throws JSONException
	{
		JSONObject o;
		
		o = jo.getJSONObject("distance");
		Distance = o.getLong("value");
		DistanceText = o.getString("text");
		
		o = jo.getJSONObject("duration");
		Duration = o.getLong("value");
		DurationText = o.getString("text");
		
		o = jo.getJSONObject("end_location");
		EndLatitude = o.getDouble("lat");
		EndLongitude = o.getDouble("lng");
		
		o = jo.getJSONObject("start_location");
		StartLatitude = o.getDouble("lat");
		StartLongitude = o.getDouble("lng");
		
		StartAddress = jo.getString("start_address");
		EndAddress = jo.getString("end_address");
		/* via_waypoint */
		
		JSONArray array;
		int i, len;
		
		array = jo.getJSONArray("steps");
		len = array.length();
		for(i = 0; i < len; i++)
		{
			DirectionsSteps steps = new DirectionsSteps();
			steps.inflateResponse(array.getJSONObject(i));
			Steps.add(steps);
		}
	}
}
