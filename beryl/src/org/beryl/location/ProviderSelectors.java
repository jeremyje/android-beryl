package org.beryl.location;

import java.util.ArrayList;
import java.util.List;

import android.location.Criteria;
import android.location.LocationManager;

public class ProviderSelectors {

	public static final IProviderListSelector AllFree;
	public static final IProviderListSelector AllFreeEnabledOnly;
	public static final IProviderListSelector GpsAndNetworkOnly;
	static final Criteria FreeProviderCriteria;
	static final Criteria AllProviderCriteria;

	static {
		FreeProviderCriteria = new Criteria();
		FreeProviderCriteria.setCostAllowed(false);

		AllProviderCriteria = new Criteria();
		AllProviderCriteria.setCostAllowed(true);

		AllFree = new AllFreeProviders();
		AllFreeEnabledOnly = new AllEnabledFreeProviders();
		GpsAndNetworkOnly = new GpsAndNetworkOnlyProviders();
	}

	private static class AllFreeProviders implements IProviderListSelector {

		public List<String> getProviders(LocationManager lm) {
			return lm.getProviders(FreeProviderCriteria, false);
		}

		public String getBestProvider(LocationManager lm) {
			return lm.getBestProvider(FreeProviderCriteria, false);
		}
	}

	public static class AllEnabledFreeProviders implements
			IProviderListSelector {

		public List<String> getProviders(LocationManager lm) {
			return lm.getProviders(FreeProviderCriteria, true);
		}

		public String getBestProvider(LocationManager lm) {
			return lm.getBestProvider(FreeProviderCriteria, true);
		}
	}

	public static class AllProviders implements IProviderListSelector {

		public List<String> getProviders(LocationManager lm) {
			return lm.getProviders(AllProviderCriteria, false);
		}

		public String getBestProvider(LocationManager lm) {
			return lm.getBestProvider(AllProviderCriteria, false);
		}
	}
	
	public static class GpsAndNetworkOnlyProviders implements IProviderListSelector {

		public List<String> getProviders(LocationManager lm) {
			List<String> availableProviders = lm.getAllProviders();
			List<String> providers = new ArrayList<String>();
			
			for(String provider : availableProviders) {
				if(provider.equals(LocationManager.GPS_PROVIDER) ||
					provider.equals(LocationManager.NETWORK_PROVIDER)) {
					providers.add(provider);
				}
			}
			
			return providers;
		}

		public String getBestProvider(LocationManager lm) {
			return lm.getBestProvider(AllProviderCriteria, false);
		}
	}
}
