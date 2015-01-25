package org.beryl.web.search.google;

import java.util.ArrayList;

import org.beryl.web.search.AbstractSearchResponseSegment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GoogleSearchResponse extends AbstractSearchResponseSegment
{
	public static GoogleSearchResponse getResponse(String response)
	{
		GoogleSearchResponse result = new GoogleSearchResponse();
		result.inflateResponse(response);
		return result;
	}

	public ArrayList<GoogleSearchResult> Results = new ArrayList<GoogleSearchResult>();
	
	protected GoogleSearchResponse()
	{
		
	}
	
	JSONObject _response;
	
	@Override
	public void inflateResponse(JSONObject jo) throws JSONException
	{
		_response = jo;
		final JSONObject sr = _response.getJSONObject("responseData");
		final JSONArray results = sr.getJSONArray("results");
		JSONObject result;
		if(results != null)
		{
			String GsearchResultClass;
			int i;
			int len = results.length();
			for(i = 0; i < len; i++)
			{
				result = (JSONObject) results.get(i);
				GsearchResultClass = result.getString("GsearchResultClass");
				if(GsearchResultClass.compareToIgnoreCase("GlocalSearch") == 0)
				{
					Results.add(new GoogleLocalSearchResult(result));
				}
			}
		}
	}
}
