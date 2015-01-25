package org.beryl.billing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.beryl.billing.Constants.ResponseCode;


import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

class BillingRequestManager implements ServiceConnection {

	public static interface IBillingRequestCompletedListener {
		void onBillingRequestsCompleted();
	}
	
	public static interface IBillingRequest {
		long run(MarketBilling marketBilling) throws RemoteException;
		void onRemoteException(RemoteException ex);
		void responseCodeReceived(ResponseCode responseCode);
	}
	
	private MarketBillingServiceConnection serviceConnection = new MarketBillingServiceConnection();
	
	private static final String TAG = "BillingRequestManager";
	private final LinkedList<IBillingRequest> queuedRequests = new LinkedList<IBillingRequest>();
	private final HashMap<Long, IBillingRequest> pendingRequests = new HashMap<Long, IBillingRequest>();

	private IBillingRequestCompletedListener completedRequestsListener = null;
	private Context context;
	
	public BillingRequestManager() {
		serviceConnection.setCallback(this);
	}
	
	public void setCompletedListener(IBillingRequestCompletedListener listener) {
		this.completedRequestsListener = listener;
	}
	
	public void setContext(final Context context) {
		this.context = context;
	}
	
	public boolean run(IBillingRequest request) {
		boolean success = false;
		
		if(isServiceAvailable()) {
			success = execute(request);
		} else {
			bindToMarketBillingService();
		}
		
		if(! success) {
			queuedRequests.add(request);
		}
		
		return success;
	}
	
	/**
	 * Binds to the MarketBillingService and returns true if the bind succeeded.
	 * 
	 * @return true if the bind succeeded; false otherwise
	 */
	private boolean bindToMarketBillingService() {
		try {
			if (Constants.DEBUG) {
				Log.i(TAG, "binding to Market billing service");
			}
			boolean bindResult = serviceConnection.bind(context);

			if (bindResult) {
				return true;
			} else {
				Log.e(TAG, "Could not bind to service.");
			}
		} catch (SecurityException e) {
			Log.e(TAG, "Security exception: " + e);
		}
		return false;
	}
	
	private boolean isServiceAvailable() {
		return serviceConnection.isConnected();
	}
	
	private boolean execute(IBillingRequest request) {
		boolean success = false;
		try {
			long requestId = request.run(serviceConnection.getMarketBillingService());
			pendingRequests.put(requestId, request);
			success = true;
		} catch(RemoteException ex) {
			request.onRemoteException(ex);
		}
		
		return success;
	}
	
	public void printDebug() {
		Log.e(TAG, "Remaing Requests and Responses");
    	Log.e(TAG, "Pending Requests: " + queuedRequests.size());
    	for(IBillingRequest r : queuedRequests) {
    		Log.e(TAG, r.getClass().getSimpleName());
    	}
        Log.e(TAG, "Sent Requests: " + pendingRequests.size());
        for(Entry<Long,IBillingRequest> r : pendingRequests.entrySet()) {
    		Log.e(TAG, r.getKey() + " - " + r.getValue().getClass().getSimpleName());
    	}
	}

	public void acceptResponse(long requestId, ResponseCode responseCode) {
		IBillingRequest request = pendingRequests.get(requestId);
		if (request != null) {
			if (Constants.DEBUG) {
				Log.d(TAG, request.getClass().getSimpleName() + ": "
						+ responseCode);
			}
			request.responseCodeReceived(responseCode);
		}
		pendingRequests.remove(requestId);
		
		checkAllRequestsCompleted();
	}

	private void checkAllRequestsCompleted() {
		if(queuedRequests.size() == 0 && pendingRequests.size() == 0) {
			if(completedRequestsListener != null)
				completedRequestsListener.onBillingRequestsCompleted();
		}
	}

	private void runQueuedRequests() {
		IBillingRequest request;
		while (!queuedRequests.isEmpty()) {
			request = queuedRequests.pop();
			boolean runSuccessful = this.run(request);
			if(! runSuccessful)
				return;
			
			checkAllRequestsCompleted();
		}
	}
	
	public void onServiceConnected(ComponentName name, IBinder service) {
		runQueuedRequests();
	}

	public void onServiceDisconnected(ComponentName name) {
	}

	public void unbind() {
		setCompletedListener(null);
		serviceConnection.unbind(context);
		this.context = null;
	}
}
