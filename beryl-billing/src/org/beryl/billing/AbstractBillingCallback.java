package org.beryl.billing;

import org.beryl.billing.Constants.PurchaseState;
import org.beryl.billing.Constants.ResponseCode;

public abstract class AbstractBillingCallback implements IBillingCallback {

	private final AbstractBillingCallback child;
	
	public AbstractBillingCallback() {
		child = null;
	}
	
	public AbstractBillingCallback(final AbstractBillingCallback child) {
		this.child = child;
	}
	
	public final void billingSupported(final boolean supported) {
		if(child != null) {
			child.billingSupported(supported);
		}
		
		onBillingSupported(supported);
	}
	
	public final void purchaseStateChanged(final PurchaseState purchaseState,
			final String itemId, final int quantity, final long purchaseTime,
			final String developerPayload) {
		if(child != null) {
			child.purchaseStateChanged(purchaseState, itemId, quantity, purchaseTime, developerPayload);
		}
		
		onPurchaseStateChanged(purchaseState, itemId, quantity, purchaseTime, developerPayload);
	}
	
	public final void requestPurchaseResponse(final BillingItem item,
			final ResponseCode responseCode) {
		if(child != null) {
			child.requestPurchaseResponse(item, responseCode);
		}
		
		onRequestPurchaseResponse(item, responseCode);
	}
	
	public final void restoreTransactionsResponse(final ResponseCode responseCode) {
		if(child != null) {
			child.restoreTransactionsResponse(responseCode);
		}
		
		onRestoreTransactionsResponse(responseCode);
	}
	
	protected abstract void onBillingSupported(final boolean supported);
	protected void onPurchaseStateChanged(final PurchaseState purchaseState,
			final String itemId, final int quantity, final long purchaseTime,
			final String developerPayload) {
	}
	protected void onRequestPurchaseResponse(final BillingItem item,
			final ResponseCode responseCode) {
	}
	protected void onRestoreTransactionsResponse(final ResponseCode responseCode) {
	}
}
