package org.beryl.web.search.google.maps;

import java.util.ArrayList;

import org.beryl.web.search.AbstractSearchResponseSegment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectionsRoute extends AbstractSearchResponseSegment
{
	public String Summary;
	public ArrayList<DirectionsLeg> Legs = new ArrayList<DirectionsLeg>();

	public String Copyrights;
	public ArrayList<String> Warnings = new ArrayList<String>();
	public ArrayList<Integer> WaypointOrder = new ArrayList<Integer>();
	
	@Override
	public void inflateResponse(JSONObject jo) throws JSONException
	{
		Summary = jo.getString("summary");
		Copyrights = jo.getString("copyrights");
		
		JSONArray array = jo.getJSONArray("legs");
		int i;
		int len = array.length();
		
		for(i = 0; i < len; i++)
		{
			DirectionsLeg leg = new DirectionsLeg();
			leg.inflateResponse(array.getJSONObject(i));
			Legs.add(leg);
		}
		
		array = jo.getJSONArray("warnings");
		len = array.length();
		for(i = 0; i < len; i++)
		{
			Warnings.add(array.getString(i));
		}
		
		array = jo.getJSONArray("waypoint_order");
		len = array.length();
		for(i = 0; i < len; i++)
		{
			WaypointOrder.add(array.getInt(i));
		}
	}

}
