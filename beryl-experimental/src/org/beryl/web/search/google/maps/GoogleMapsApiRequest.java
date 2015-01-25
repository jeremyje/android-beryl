package org.beryl.web.search.google.maps;

import java.util.Locale;

import android.location.Location;
import android.os.Bundle;

public class GoogleMapsApiRequest
{
	public static String fromLatitudeLongitudePair(final double latitude, final double longitude)
	{
		return String.format(Locale.ENGLISH, "%f,%f", latitude, longitude);
	}
	
	public static String fromLocation(Location point)
	{
		return fromLatitudeLongitudePair(point.getLatitude(), point.getLongitude());
	}

	public static Bundle createDirectionsRequest(final String origin, final String destination, final boolean used_sensor)
	{
		final Bundle result = new Bundle();
		result.putString("origin", origin);
		result.putString("destination", destination);
		result.putBoolean("sensor", used_sensor);
		return result;
	}
	
	public static void setDirectionsRequestExtras(final Bundle request, final String navigation_mode, final boolean provide_alternative_paths, final String avoid_restrictions)
	{
		request.putString("mode", navigation_mode);
		request.putBoolean("alternatives", provide_alternative_paths);
		if(avoid_restrictions != null)
		{	
			request.putString("avoid", avoid_restrictions);
		}
	}

}
