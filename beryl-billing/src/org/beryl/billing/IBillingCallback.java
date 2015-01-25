package org.beryl.billing;

import org.beryl.app.RegisterableContract;
import org.beryl.billing.Constants.PurchaseState;
import org.beryl.billing.Constants.ResponseCode;

interface IBillingCallback extends RegisterableContract {
	void billingSupported(boolean supported);
	void purchaseStateChanged(PurchaseState purchaseState, String itemId,
            int quantity, long purchaseTime, String developerPayload);
	void requestPurchaseResponse(BillingItem model,
            ResponseCode responseCode);
	void restoreTransactionsResponse(ResponseCode responseCode);
}
