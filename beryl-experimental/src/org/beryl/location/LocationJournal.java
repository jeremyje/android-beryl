package org.beryl.location;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Stores a catalog of all the most recent points that came from a Location Provider.
 * Journals only accept locations from 1 provider. Data points from other providers will be ignored.
 */
public class LocationJournal implements LocationListener {

	private static final int DEFAULT_MAX_LOCATIONS = 100;

	private final String _providerFilter;
	public final List<Location> History;
	private final int _maxLocations;

	public LocationJournal(final String provider) {
		_providerFilter = provider;
		_maxLocations = DEFAULT_MAX_LOCATIONS;

		final ArrayList<Location> locations = new ArrayList<Location>();
		locations.ensureCapacity(_maxLocations);
		History = locations;
	}

	public LocationJournal(final String provider, final int maxLocations) {
		_providerFilter = provider;
		_maxLocations = maxLocations;

		final ArrayList<Location> locations = new ArrayList<Location>();
		locations.ensureCapacity(_maxLocations);
		History = locations;
	}

	public void onLocationChanged(final Location location) {
		if(location.getProvider().equals(_providerFilter)) {
			add(location);
		}
	}

	public void onProviderDisabled(final String provider) {
	}

	public void onProviderEnabled(final String provider) {
	}

	public void onStatusChanged(final String provider, final int status, final Bundle extras) {
	}

	public void clear() {
		History.clear();
	}

	private void add(final Location location) {
		// FIXME: This is probably really inefficient.
		if(History.size() == _maxLocations) {
			History.remove(0);
		}
		History.add(location);
	}
}
