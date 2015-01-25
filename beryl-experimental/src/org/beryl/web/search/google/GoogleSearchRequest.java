package org.beryl.web.search.google;

import java.util.Locale;

import android.os.Bundle;

public class GoogleSearchRequest
{
	public static Bundle createSearchRequest(final String api_key, final String search_query)
	{
		final Bundle result = new Bundle();
		
		result.putString("key", api_key);
		result.putString("q", search_query);
		result.putString("v", "1.0");
		return result;
	}
	
	public static void setResultRange(final Bundle request, int num_results, int result_start)
	{
		request.putInt("rsz", num_results);
		request.putInt("start", result_start);
	}
	
	public static void setTargetLanguage(final Bundle request, final String host_language)
	{
		request.putString("hl", host_language);
	}
	public static void setExtraParameters(final Bundle request, int num_results, int result_start, final String host_language)
	{
		setResultRange(request, num_results, result_start);
		setTargetLanguage(request, host_language);
	}
	
	public static void setWebSearch(final Bundle request, String safe_mode, String specific_country)
	{
		request.putString("safe", safe_mode);
		request.putString("gl", specific_country);
	}
	
	public static void setLocalSearch(final Bundle request, double latitude, double longitude, double neToSeLatitudeDelta, double neToSeLongitudeDelta, boolean include_kml)
	{
		request.putString("sll", String.format(Locale.ENGLISH, "%.5f,%.5f", latitude, longitude));
		request.putString("sspn", String.format(Locale.ENGLISH, "%.5f,%.5f", neToSeLatitudeDelta, neToSeLongitudeDelta));
		if (include_kml)
		{
			request.putString("mrt", "blended");
		}
		else
		{
			request.putString("mrt", "localonly");
		}	
	}
}
