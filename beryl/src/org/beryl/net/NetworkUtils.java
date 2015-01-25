package org.beryl.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/** Collection of network utility methods for */
public class NetworkUtils {

	private static ConnectivityManager getConnectivityManager(final Context context) {
		return (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	private static NetworkInfo getCurrentNetwork(final Context context) {
		ConnectivityManager mgr = getConnectivityManager(context);
		return mgr.getActiveNetworkInfo();
	}
	
	/** Returns true if the network is available for syncing (in the background) small amounts of data.
	 * For example a news feed. */
	@SuppressWarnings("deprecation")
	public static boolean canSyncData(final Context context) {
		return isConnected(context) && getConnectivityManager(context).getBackgroundDataSetting();
	}
	
	public static boolean canSyncLargeData(final Context context) {
		return isNetworkFast(context) && canSyncData(context);
	}
	/** Returns true if a network connection is available but the bandwidth is possibly metered or costly. */
	public static boolean isNetworkConstrained(final Context context) {
		return ! isNetworkFast(context) && isConnected(context);
	}
	
	/** Returns true if a network connection is available and it is fast. (Ethernet or Wifi). */
	public static boolean isNetworkFast(final Context context) {
		final NetworkInfo info = getCurrentNetwork(context);
		if(info != null) {
			if(! info.isRoaming() && 
					(info.getType() == ConnectivityManager.TYPE_ETHERNET || 
					 info.getType() == ConnectivityManager.TYPE_WIFI)) {
				return true;
			}
		}
		
		return false;
	}
	
	/** Return true if a network is available. */
	public static boolean isConnected(final Context context) {
		final NetworkInfo info = getCurrentNetwork(context);
		if(info != null) {
			return info.isConnected();
		} else {
			return false;
		}
	}
}
