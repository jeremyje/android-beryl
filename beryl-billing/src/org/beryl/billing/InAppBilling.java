package org.beryl.billing;

import java.lang.reflect.Method;

import org.beryl.app.RegisterableContract;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

public class InAppBilling {

	static interface IPurchaseRequestCallback extends RegisterableContract {
		void startBuyPageActivity(PendingIntent pendingIntent, Intent intent);
	}
	
	private static class PurchasePageCallback implements IPurchaseRequestCallback {

		private final String TAG = "PurchasePageCallback";
		private Method mStartIntentSender;
	    private Object[] mStartIntentSenderArgs = new Object[5];
	    private static final Class<?>[] START_INTENT_SENDER_SIG = new Class<?>[] {
	        IntentSender.class, Intent.class, int.class, int.class, int.class
	    };
	    
		private final Activity activity;
		
		PurchasePageCallback(Activity activity) {
			this.activity = activity;
			initCompatibilityLayer();
		}
		
		private void initCompatibilityLayer() {
	        try {
	            mStartIntentSender = activity.getClass().getMethod("startIntentSender",
	                    START_INTENT_SENDER_SIG);
	        } catch (SecurityException e) {
	            mStartIntentSender = null;
	        } catch (NoSuchMethodException e) {
	            mStartIntentSender = null;
	        }
	    }
		
		public void startBuyPageActivity(PendingIntent pendingIntent,
				Intent intent) {
			if (mStartIntentSender != null) {
	            // This is on Android 2.0 and beyond.  The in-app buy page activity
	            // must be on the activity stack of the application.
	            try {
	                // This implements the method call:
	                // mActivity.startIntentSender(pendingIntent.getIntentSender(),
	                //     intent, 0, 0, 0);
	                mStartIntentSenderArgs[0] = pendingIntent.getIntentSender();
	                mStartIntentSenderArgs[1] = intent;
	                mStartIntentSenderArgs[2] = Integer.valueOf(0);
	                mStartIntentSenderArgs[3] = Integer.valueOf(0);
	                mStartIntentSenderArgs[4] = Integer.valueOf(0);
	                mStartIntentSender.invoke(activity, mStartIntentSenderArgs);
	            } catch (Exception e) {
	                Log.e(TAG, "error starting activity", e);
	            }
	        } else {
	            // This is on Android version 1.6. The in-app buy page activity must be on its
	            // own separate activity stack instead of on the activity stack of
	            // the application.
	            try {
	                pendingIntent.send(activity, 0 /* code */, intent);
	            } catch (CanceledException e) {
	                Log.e(TAG, "error starting activity", e);
	            }
	        }
		}
	}
	private Context context;
	private IPurchaseRequestCallback purchaseCallback;
	private IBillingCallback callback;
	
	public InAppBilling(final Context context) {
		this.context = context;
	}
	
	public void start(final AbstractBillingCallback callback, final Activity activity) {
		this.callback = callback;
		this.purchaseCallback = new PurchasePageCallback(activity);
		ResponseHandler.register(callback);
		ResponseHandler.register(purchaseCallback);
	}
	
	public void stop() {
		ResponseHandler.unregister(callback);
		ResponseHandler.unregister(purchaseCallback);
	}

	public void checkBillingSupported() {
		BillingService.checkBillingSupported(context);
	}
	
	public void restoreTransactions() {
		BillingService.restoreTransactions(context);
	}
	
	public void beginPurchase(String productId) {
		beginPurchase(productId, null);
	}
	
	public void beginPurchase(String productId, String developerPayload) {
		BillingService.requestPurchase(context, new BillingItem(productId, developerPayload));
	}
}
