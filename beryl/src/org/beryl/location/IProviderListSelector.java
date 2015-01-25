package org.beryl.location;

import java.util.List;

import android.location.LocationManager;

public interface IProviderListSelector {
	String getBestProvider(LocationManager lm);
	List<String> getProviders(LocationManager lm);
}
