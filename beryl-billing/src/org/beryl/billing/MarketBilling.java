package org.beryl.billing;

import org.beryl.billing.Constants.ResponseCode;
import org.beryl.diagnostics.Logger;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.android.vending.billing.IMarketBillingService;

class MarketBilling {

	private static final String TAG = "MarketBilling";
	private IMarketBillingService mService;
	private Context mContext;
	
	public MarketBilling(Context context, IBinder service) {
		mContext = context;
		mService = IMarketBillingService.Stub.asInterface(service);
	}
	
	public boolean checkBillingSupported() throws RemoteException {
		Bundle response = callMethod("CHECK_BILLING_SUPPORTED");
		int responseCode = response
				.getInt(Constants.BILLING_RESPONSE_RESPONSE_CODE);
		if (Constants.DEBUG) {
			Log.i(TAG,
					"CheckBillingSupported response code: "
							+ ResponseCode.valueOf(responseCode));
		}
		boolean billingSupported = (responseCode == ResponseCode.RESULT_OK
				.ordinal());
		return billingSupported;
	}

	public Bundle requestPurchase(String productId, String developerPayload)
			throws RemoteException {
		Bundle request = getMethod("REQUEST_PURCHASE");
		request.putString(Constants.BILLING_REQUEST_ITEM_ID, productId);
		// Note that the developer payload is optional.
		if (developerPayload != null) {
			request.putString(Constants.BILLING_REQUEST_DEVELOPER_PAYLOAD,
					developerPayload);
		}

		Bundle response = callMethod(request);
		return response;
	}
	
	public long confirmNotifications(final String[] notificationIds)
			throws RemoteException {
		
		Bundle request = getMethod("CONFIRM_NOTIFICATIONS");
		request.putStringArray(Constants.BILLING_REQUEST_NOTIFY_IDS, notificationIds);
		Bundle response = callMethod(request);
		return response.getLong(Constants.BILLING_RESPONSE_REQUEST_ID,
				Constants.BILLING_RESPONSE_INVALID_REQUEST_ID);
	}
	
	public long getPurchaseInformation(long nonce, final String[] notificationIds) throws RemoteException {
		Bundle request = getMethod("GET_PURCHASE_INFORMATION");
		request.putLong(Constants.BILLING_REQUEST_NONCE, nonce);
		request.putStringArray(Constants.BILLING_REQUEST_NOTIFY_IDS,
				notificationIds);
		Bundle response = callMethod(request);
		return response.getLong(Constants.BILLING_RESPONSE_REQUEST_ID,
				Constants.BILLING_RESPONSE_INVALID_REQUEST_ID);
	}
	
	public long restoreTransactions(long nonce) throws RemoteException {
		Bundle request = getMethod("RESTORE_TRANSACTIONS");
		request.putLong(Constants.BILLING_REQUEST_NONCE, nonce);
		Bundle response = callMethod(request);
		return response.getLong(Constants.BILLING_RESPONSE_REQUEST_ID,
				Constants.BILLING_RESPONSE_INVALID_REQUEST_ID);
	}

	private Bundle sendBillingRequest(Bundle bundle) throws RemoteException {
		Bundle response;
		Logger.d("sendBillingRequest()");
		Logger.d(bundle);
		response = mService.sendBillingRequest(bundle);
		Logger.d(response);
		return response;
	}
	
	private Bundle callMethod(Bundle methodAndParams) throws RemoteException {
		return sendBillingRequest(methodAndParams);
	}
	
	private Bundle callMethod(String method) throws RemoteException {
		final Bundle request = getMethod(method);
		return sendBillingRequest(request);
	}

	private Bundle getMethod(String method) {
		Bundle request = new Bundle();
		request.putString(Constants.BILLING_REQUEST_METHOD, method);
		request.putInt(Constants.BILLING_REQUEST_API_VERSION, 1);
		request.putString(Constants.BILLING_REQUEST_PACKAGE_NAME, mContext.getPackageName());
		return request;
	}
}
