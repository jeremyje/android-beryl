package org.beryl.billing;

import org.beryl.billing.Constants.PurchaseState;

import android.database.Cursor;

public interface IPurchaseDatastore {

	void close();

	/**
	 * Adds the given purchase information to the database and returns the total
	 * number of times that the given product has been purchased.
	 * @param orderId a string identifying the order
	 * @param productId the product ID (sku)
	 * @param purchaseState the purchase state of the product
	 * @param purchaseTime the time the product was purchased, in milliseconds
	 * since the epoch (Jan 1, 1970)
	 * @param developerPayload the developer provided "payload" associated with
	 *     the order
	 * @return the number of times the given product has been purchased.
	 */
	int updatePurchase(String orderId, String productId,
			PurchaseState purchaseState, long purchaseTime,
			String developerPayload);

	/**
	 * Returns a cursor that can be used to read all the rows and columns of
	 * the "purchased items" table.
	 */
	Cursor queryAllPurchasedItems();

}