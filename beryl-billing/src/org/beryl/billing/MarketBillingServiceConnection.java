package org.beryl.billing;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

class MarketBillingServiceConnection implements ServiceConnection {

	private static final String TAG = "MarketBillingServiceConnection";
	
	private Context context = null;
	private ServiceConnection callback = null;
	private MarketBilling billing = null;
	
	public void setCallback(ServiceConnection callback) {
		this.callback = callback;
	}
	
	public void onServiceConnected(ComponentName name, IBinder service) {
		if (Constants.DEBUG) {
			Log.d(TAG, "Billing service connected");
		}

		billing = new MarketBilling(context, service);
		
		if(callback != null) {
			callback.onServiceConnected(name, service);
		}
	}

	public void onServiceDisconnected(ComponentName name) {
		Log.w(TAG, "Billing service disconnected");
		billing = null;
		
		if(callback != null){
			callback.onServiceDisconnected(name);
		}
	}

	public boolean bind(Context context) {
		this.context = context;
		return context.bindService(new Intent(
				Constants.MARKET_BILLING_SERVICE_ACTION), this,
				Context.BIND_AUTO_CREATE);
	}

	public void unbind(Context context) {
		try {
			context.unbindService(this);
		} catch (IllegalArgumentException e) {
			// This might happen if the service was disconnected
		}
	}

	public boolean isConnected() {
		return billing != null;
	}
	
	public MarketBilling getMarketBillingService() {
		return billing;
	}
}
