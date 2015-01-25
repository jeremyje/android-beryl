package org.beryl.web.search.google.maps;

import org.beryl.web.search.AbstractSearchResponseSegment;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectionsSteps extends AbstractSearchResponseSegment
{
	public long Distance;
	public String DistanceText;
	public long Duration;
	public String DurationText;
	
	public double StartLatitude;
	public double StartLongitude;
	public double EndLatitude;
	public double EndLongitude;
	
	public String HtmlInstructions;
	public String TravelMode;
	
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
		
		TravelMode = jo.getString("travel_mode");
		
		HtmlInstructions = jo.getString("html_instructions");
	}
}
