package org.beryl.billing;

import org.beryl.billing.Constants.PurchaseState;
import org.beryl.billing.Constants.ResponseCode;

import android.util.Log;

public class DebugLogBillingCallback extends AbstractBillingCallback {
	private static final String TAG = "BillingCallback";
	
	private void log(String message) {
		if (Constants.DEBUG) {
			Log.i(TAG, message);
        }
	}
	
	public DebugLogBillingCallback() {
		super();
	}
	
	public DebugLogBillingCallback(AbstractBillingCallback child) {
		super(child);
	}
	
	@Override
	protected void onBillingSupported(boolean supported) {
		log("onBillingSupported(supported= " + supported + ")");
	}

	@Override
	protected void onPurchaseStateChanged(PurchaseState purchaseState,
			String itemId, int quantity, long purchaseTime,
			String developerPayload) {
		log("onPurchaseStateChange(" + 
			"purchaseState= " + purchaseState.name() +
			",itemId= " + itemId +
			",quantity= " + quantity +
			",purchaseTime= " + purchaseTime +
			",developerPayload= " + developerPayload + ")");
	}

	@Override
	protected void onRequestPurchaseResponse(BillingItem model,
			ResponseCode responseCode) {
		log("onRequestPurchaseResponse(" + 
				"model= " + model.toString() +
				",responseCode= " + responseCode.name() + ")");
	}

	@Override
	protected void onRestoreTransactionsResponse(ResponseCode responseCode) {
		log("onRestoreTransactionsResponse(" + 
				"responseCode= " + responseCode.name() + ")");
	}
}
