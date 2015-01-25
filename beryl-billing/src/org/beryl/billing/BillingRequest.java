package org.beryl.billing;

import org.beryl.billing.Constants.ResponseCode;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

abstract class BillingRequest implements BillingRequestManager.IBillingRequest {
	private static final String TAG = "BillingRequest";
	
	public BillingRequest(int startId) {
		//mStartId = startId;
	}

	/**
	 * Called when a remote exception occurs while trying to execute the
	 * {@link #run()} method. The derived class can override this to execute
	 * exception-handling code.
	 * 
	 * @param e the exception
	 */
	public void onRemoteException(RemoteException e) {
		Log.w(TAG, "remote billing service crashed");
	}
	
	/**
     * This is called when Android Market sends a response code for this
     * request.
     * @param responseCode the response code
     */
    public void responseCodeReceived(ResponseCode responseCode) {
    }
    
    /**
	 * Wrapper class that checks if in-app billing is supported.
	 */
	static class CheckBillingSupported extends BillingRequest {
		public CheckBillingSupported() {
			// This object is never created as a side effect of starting this
			// service so we pass -1 as the startId to indicate that we should
			// not stop this service after executing this request.
			super(-1);
		}

		public long run(MarketBilling marketBilling) throws RemoteException {
			boolean billingSupported = marketBilling.checkBillingSupported();
			ResponseHandler.checkBillingSupportedResponse(billingSupported);
			return Constants.BILLING_RESPONSE_INVALID_REQUEST_ID;
		}
	}

	/**
	 * Wrapper class that requests a purchase.
	 */
	static class RequestPurchase extends BillingRequest {
		public final BillingItem item;

		/*
		 * public RequestPurchase(String itemId) { this(itemId, null); }
		 */
		public RequestPurchase(BillingItem item) {
			// This object is never created as a side effect of starting this
			// service so we pass -1 as the startId to indicate that we should
			// not stop this service after executing this request.
			super(-1);
			this.item = item;
		}

		public long run(MarketBilling marketBilling) throws RemoteException {
			Bundle response = marketBilling.requestPurchase(item.productId,
					item.developerPayload);

			PendingIntent pendingIntent = response
					.getParcelable(Constants.BILLING_RESPONSE_PURCHASE_INTENT);
			if (pendingIntent == null) {
				Log.e(TAG, "Error with requestPurchase");
				return Constants.BILLING_RESPONSE_INVALID_REQUEST_ID;
			}

			Intent intent = new Intent();
			ResponseHandler.buyPageIntentResponse(pendingIntent, intent);
			return response.getLong(Constants.BILLING_RESPONSE_REQUEST_ID,
					Constants.BILLING_RESPONSE_INVALID_REQUEST_ID);
		}

		@Override
		public void responseCodeReceived(ResponseCode responseCode) {
			ResponseHandler.responseCodeReceived(item, responseCode);
		}
	}

	/**
	 * Wrapper class that confirms a list of notifications to the server.
	 */
	static class ConfirmNotifications extends BillingRequest {
		final String[] mNotifyIds;

		public ConfirmNotifications(int startId, String[] notifyIds) {
			super(startId);
			mNotifyIds = notifyIds;
		}

		public long run(MarketBilling marketBilling) throws RemoteException {
			return marketBilling.confirmNotifications(mNotifyIds);
		}
	}

	/**
	 * Wrapper class that sends a GET_PURCHASE_INFORMATION message to the
	 * server.
	 */
	static class GetPurchaseInformation extends BillingRequest {
		long mNonce;
		final String[] mNotifyIds;

		public GetPurchaseInformation(int startId, String[] notifyIds) {
			super(startId);
			mNotifyIds = notifyIds;
		}

		public long run(MarketBilling marketBilling) throws RemoteException {
			mNonce = Security.generateNonce();

			return marketBilling.getPurchaseInformation(mNonce, mNotifyIds);
		}

		@Override
		public void onRemoteException(RemoteException e) {
			super.onRemoteException(e);
			Security.removeNonce(mNonce);
		}
	}

	/**
	 * Wrapper class that sends a RESTORE_TRANSACTIONS message to the server.
	 */
	static class RestoreTransactions extends BillingRequest {
		long mNonce;

		public RestoreTransactions() {
			// This object is never created as a side effect of starting this
			// service so we pass -1 as the startId to indicate that we should
			// not stop this service after executing this request.
			super(-1);
		}

		public long run(MarketBilling marketBilling) throws RemoteException {
			mNonce = Security.generateNonce();

			return marketBilling.restoreTransactions(mNonce);
		}

		@Override
		public void onRemoteException(RemoteException e) {
			super.onRemoteException(e);
			Security.removeNonce(mNonce);
		}

		@Override
		public void responseCodeReceived(ResponseCode responseCode) {
			ResponseHandler.responseCodeReceivedForRestoreTransactions(responseCode);
		}
	}
}
