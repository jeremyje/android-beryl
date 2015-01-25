package org.beryl.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

/** Listens to the number of satellites that are currently visible. Can be used to determine the strength of the GPS signal. */
public class GpsSignalStrengthMonitor implements LocationListener {

	private int _numSatellites = 0;

	public void onLocationChanged(Location location) {
	}

	public void onProviderDisabled(String provider) {
		if(provider.equals(LocationManager.GPS_PROVIDER)) {
			_numSatellites = 0;
		}
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		boolean is_available = LocationProvider.AVAILABLE == status;

		if(provider.equals(LocationManager.GPS_PROVIDER)) {
			if(is_available){
				_numSatellites = extras.getInt("satellites");
			}
			else {
				_numSatellites = 0;
			}
		}
	}

	public boolean isAvailable() {
		return _numSatellites != 0;
	}

	public int getSatellites() {
		return _numSatellites;
	}

	/** Provides a 0-5 bar rating for the signal strength of GPS. Similar to the bars that indicate cellphone reception. */
	public int getZeroFiveBarRating() {
		if(_numSatellites < 3)
			return 0;
		else if(_numSatellites < 4)
			return 1;
		else if(_numSatellites < 5)
			return 2;
		else if(_numSatellites < 6)
			return 3;
		else if(_numSatellites < 7)
			return 4;
		return 5;
	}
}
