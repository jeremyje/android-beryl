package org.beryl.location;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class LocationMonitor implements LocationListener {

	private final List<LocationListener> listeners = new ArrayList<LocationListener>();
	private IProviderListSelector providerSelector;
	private final LocationManager lm;
	private Controller controller = new Controller();
	
	public LocationMonitor(final Context context) {
		providerSelector = ProviderSelectors.AllFree;
		lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	}

	public void setProviderSelector(IProviderListSelector selector) {
		providerSelector = selector;
	}
	
	public boolean hasAtLeastOneEnabledProvider() {
		return getNumberOfEnabledSelectedProviders() > 0;
	}
	
	public int getNumberOfEnabledSelectedProviders() {
		List<String> providers = providerSelector.getProviders(lm);
		int enabledCount = 0;
		for(String provider : providers) {
			if(lm.isProviderEnabled(provider)) {
				enabledCount++;
			}
		}
		
		return enabledCount;
	}
	
	public void addListener(LocationListener listener) {
		
		// TODO: Controllers should obtain the controller class here.
		if(! listeners.contains(listener) && this != listener) {
			listeners.add(listener);
			
			if(listener instanceof ILocationMonitorController) {
				ILocationMonitorController lmc = (ILocationMonitorController)listener;
				lmc.obtainController(controller);
			}
		}
	}
	
	public void clearListeners() {
		listeners.clear();
	}
	
	/** Constantly monitoring for location updates as fast as possible.
	 * WARNING: Do not use this mode for an extensive amount of time as this is very battery intensive. */
	public void startListening() {
		startListening(0, 0.0f);
	}
	
	/** Begin monitoring for location updates using the minimum time and distance intervals. */
	public void startListening(long minTime, float minDistance) {
		List<String> providers = providerSelector.getProviders(lm);
		
		for(String provider : providers) {
			lm.requestLocationUpdates(provider, minTime, minDistance, this);
		}
	}
	
	/** Stop monitoring for location updates. */
	public void stopListening() {
		lm.removeUpdates(this);
	}

	/** Returns true if the device supports the specified location provider. */
	public boolean isProviderSupported(String providerName) {
		final LocationProvider provider = lm.getProvider(providerName);
		return provider == null;
	}
	
	/** Returns true if the device is GPS capable. */
	public boolean isGpsSupported() {
		return isProviderSupported(LocationManager.GPS_PROVIDER);
	}
	
	/** Returns true if the device can geo-locate based on network. */
	public boolean isNetworkSupported() {
		return isProviderSupported(LocationManager.NETWORK_PROVIDER);
	}
	
	/** Returns true if GPS is available and enabled on the device. */
	public boolean isGpsEnabled() {
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/** Returns true if network triangulation is available and enabled on the device. */
	public boolean isNetworkEnabled() {
		return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	/** Returns true if a specific provider is enabled on the device. */
	public boolean isProviderEnabled(final String provider) {
		return lm.isProviderEnabled(provider);
	}

	/** Returns true if network triangulation or gps is available and enabled on the device. */
	public boolean isNetworkOrGpsEnabled() {
		return isNetworkEnabled() || isGpsEnabled();
	}
	
	/** Gets a list of all enabled location providers. */
	public List<String> getEnabledProviders() {
		return lm.getProviders(true);
	}

	/** Returns true if any location provider is enabled on the device at this time. */
	public boolean isAnyProviderEnabled() {
		return getEnabledProviders().size() > 0;
	}
	
	/** Gets the best provider that is usable by the system at this point in time. */
	public String getBestEnabledProvider() {
		if(isGpsEnabled())
			return LocationManager.GPS_PROVIDER;
		else if(isNetworkEnabled()) {
			return LocationManager.NETWORK_PROVIDER;
		}
		else {
			final Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
			criteria.setCostAllowed(false);
			return lm.getBestProvider(criteria, true);
		}
	}
	
	/** Gets the best location that the device has from recent memory.
	 * The availability and accuracy of the result is not guaranteed.
	 * Use this as a reasonable starting point while the location providers attempt to obtain a fix.
	 * @return Last known location.
	 */
	public Location getBestStaleLocation() {
		Location location = null;
		final long currentTime = System.currentTimeMillis();
		
		final List<String> providers = lm.getAllProviders();
		for(String provider : providers) {
			final Location test = lm.getLastKnownLocation(provider);
			if(test != null) {
				if(location == null) {
					location = test;
				} else {
					long testTime = currentTime - test.getTime();
					long locationTime = currentTime - location.getTime();

					if(test.getAccuracy() / testTime > location.getAccuracy() / locationTime) {
						location = test;
					}
				}
			}
		}
		return location;
	}
	
	/** Base class for objects that can control LocationMonitors. */
	public class Controller {
		
		protected String getBestEnabledProvider() {
			return LocationMonitor.this.getBestEnabledProvider();
		}
		
		protected Location getBestStaleLocation() {
			return LocationMonitor.this.getBestStaleLocation();
		}
		
		protected void stopListening() {
			LocationMonitor.this.stopListening();
		}
	}
	
	public void onLocationChanged(Location location) {
		for(LocationListener listener : listeners) {
			listener.onLocationChanged(location);
		}
	}

	public void onProviderDisabled(String provider) {
		for(LocationListener listener : listeners) {
			listener.onProviderDisabled(provider);
		}
	}

	public void onProviderEnabled(String provider) {
		for(LocationListener listener : listeners) {
			listener.onProviderEnabled(provider);
		}
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		for(LocationListener listener : listeners) {
			listener.onStatusChanged(provider, status, extras);
		}
	}
}
