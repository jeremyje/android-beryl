// Copyright 2010 Google Inc. All Rights Reserved.

package org.beryl.billing;

import java.util.LinkedList;
import java.util.List;

import org.beryl.Lazy;
import org.beryl.app.ContractRegistry;
import org.beryl.app.RegisterableContract;
import org.beryl.billing.Constants.PurchaseState;
import org.beryl.billing.Constants.ResponseCode;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * This class contains the methods that handle responses from Android Market.
 * The implementation of these methods is specific to a particular application.
 * The methods in this example update the database and, if the main application
 * has registered a {@link PurchaseObserver}, will also update the UI. An
 * application might also want to forward some responses on to its own server,
 * and that could be done here (in a background thread) but this example does
 * not do that.
 * 
 * You should modify and obfuscate this code before using it.
 */
class ResponseHandler {

	private static final Lazy<ContractRegistry> contracts = new Lazy<ContractRegistry>() {
		@Override
		protected ContractRegistry onSet() {
			return new ContractRegistry();
		}
	};
	
	static class PendingPurchaseIntent {
		final PendingIntent pendingIntent;
		final Intent intent;

		public PendingPurchaseIntent(PendingIntent pendingIntent, Intent intent) {
			this.pendingIntent = pendingIntent;
			this.intent = intent;
		}
	}

	private static LinkedList<PendingPurchaseIntent> pendingPurchases = new LinkedList<PendingPurchaseIntent>();

	private static final List<IBillingCallback> getCallbacks() {
		final ContractRegistry registry = contracts.get();
		return registry.getAll(IBillingCallback.class);
	}

	private static final InAppBilling.IPurchaseRequestCallback getPurchaseCallback() {
		final ContractRegistry registry = contracts.get();
		return registry.get(InAppBilling.IPurchaseRequestCallback.class);
	}

	static synchronized void register(RegisterableContract observer) {
		final ContractRegistry registry = contracts.get();
		registry.add(observer);
		attemptRunPendingPurchases();
	}

	private static void attemptRunPendingPurchases() {
		List<PendingPurchaseIntent> pending = new LinkedList<PendingPurchaseIntent>();
		pending.addAll(pendingPurchases);
		pendingPurchases.clear();
		for(PendingPurchaseIntent ppi : pending) {
			buyPageIntentResponse(ppi.pendingIntent, ppi.intent);
		}
	}

	static synchronized void unregister(RegisterableContract observer) {
		final ContractRegistry registry = contracts.get();
		registry.remove(observer);
	}

	static void checkBillingSupportedResponse(final boolean supported) {
		final List<IBillingCallback> callbacks = getCallbacks();
		for (IBillingCallback callback : callbacks) {
			callback.billingSupported(supported);
		}
	}

	static void buyPageIntentResponse(PendingIntent pendingIntent, Intent intent) {
		final InAppBilling.IPurchaseRequestCallback callback = getPurchaseCallback();
		if (callback != null) {
			callback.startBuyPageActivity(pendingIntent, intent);
		} else {
			pendingPurchases.add(new PendingPurchaseIntent(pendingIntent,
					intent));
		}
	}

	static void purchaseResponse(final Context context,
			final PurchaseState purchaseState, final String productId,
			final String orderId, final long purchaseTime,
			final String developerPayload) {

		final List<IBillingCallback> callbacks = getCallbacks();
		for (IBillingCallback callback : callbacks) {
			callback.purchaseStateChanged(purchaseState, productId, 1,
					purchaseTime, developerPayload);
		}
		/*
		 * // Update the database with the purchase state. We shouldn't do that
		 * // from the main thread so we do the work in a background thread. //
		 * We don't update the UI here. We will update the UI after we update //
		 * the database because we need to read and update the current quantity
		 * // first. new Thread(new Runnable() { public void run() { final
		 * PurchaseDatabase db = new PurchaseDatabase(context); int quantity =
		 * db.updatePurchase( orderId, productId, purchaseState, purchaseTime,
		 * developerPayload); db.close();
		 * 
		 * // This needs to be synchronized because the UI thread can change the
		 * // value of sPurchaseObserver. synchronized(ResponseHandler.class) {
		 * 
		 * if (sPurchaseObserver != null) { sPurchaseObserver } } } }).start();
		 */
	}

	static void responseCodeReceived(BillingItem itemModel,
			ResponseCode responseCode) {
		final List<IBillingCallback> callbacks = getCallbacks();
		for (IBillingCallback callback : callbacks) {
			callback.requestPurchaseResponse(itemModel, responseCode);
		}
	}

	static void responseCodeReceivedForRestoreTransactions(
			ResponseCode responseCode) {
		final List<IBillingCallback> callbacks = getCallbacks();
		for (IBillingCallback callback : callbacks) {
			callback.restoreTransactionsResponse(responseCode);
		}
	}
}
