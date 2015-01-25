package org.beryl.billing;

import java.util.ArrayList;

import org.beryl.app.ServiceCompat;
import org.beryl.app.ServiceForegrounder;
import org.beryl.billing.BillingRequestManager.IBillingRequest;
import org.beryl.billing.BillingRequestManager.IBillingRequestCompletedListener;
import org.beryl.billing.Constants.PurchaseState;
import org.beryl.billing.Constants.ResponseCode;
import org.beryl.billing.Security.VerifiedPurchase;
import org.beryl.market.test.MarketBillingTestActivity;

import com.futonredemption.example.dungeons.R;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

public class BillingService extends ServiceCompat {
	private static final String TAG = "BillingService";
	private ServiceForegrounder foreground;
	private static BillingRequestManager manager = new BillingRequestManager();
	
	private IBillingRequestCompletedListener requestsCompletedListener = new IBillingRequestCompletedListener() {
		public void onBillingRequestsCompleted() {
			manager.setCompletedListener(null);
			stopSelf();
		}
	};
	
	@Override
	protected int handleOnStartCommand(Intent intent, int flags, int startId) {
		final String action = intent.getAction();
		startForeground(action);
		manager.setContext(this);
		manager.setCompletedListener(requestsCompletedListener);
		
		if(Constants.ACTION_METHOD_CHECK_BILLING_SUPPORTED.equals(action)) {
			checkBillingSupported();
		} else if(Constants.ACTION_METHOD_REQUEST_PURCHASE.equals(action)) {
			BillingItem item = intent.getParcelableExtra("parameter");
			requestPurchase(item);
		} else if (Constants.ACTION_METHOD_RESTORE_TRANSACTIONS.equals(action)) {
			restoreTransactions();
		} else if (Constants.ACTION_CONFIRM_NOTIFICATION.equals(action)) {
			String[] notifyIds = intent
					.getStringArrayExtra(Constants.NOTIFICATION_ID);
			confirmNotifications(startId, notifyIds);
		} else if (Constants.ACTION_GET_PURCHASE_INFORMATION.equals(action)) {
			String notifyId = intent.getStringExtra(Constants.NOTIFICATION_ID);
			getPurchaseInformation(startId, new String[] { notifyId });
		} else if (Constants.ACTION_PURCHASE_STATE_CHANGED.equals(action)) {
			String signedData = intent.getStringExtra(Constants.INAPP_SIGNED_DATA);
			String signature = intent.getStringExtra(Constants.INAPP_SIGNATURE);
			purchaseStateChanged(startId, signedData, signature);
		} else if (Constants.ACTION_RESPONSE_CODE.equals(action)) {
			long requestId = intent.getLongExtra(Constants.INAPP_REQUEST_ID, -1);
			int responseCodeIndex = intent.getIntExtra(
					Constants.INAPP_RESPONSE_CODE,
					ResponseCode.RESULT_ERROR.ordinal());
			ResponseCode responseCode = ResponseCode.valueOf(responseCodeIndex);
			checkResponseCode(requestId, responseCode);
		}
		
		manager.printDebug();
		
		return Service.START_NOT_STICKY;
	}
	
	/**
	 * Checks if in-app billing is supported.
	 * 
	 * @return true if supported; false otherwise
	 */
	private boolean checkBillingSupported() {
		return runRequest(new BillingRequest.CheckBillingSupported());
	}

	private boolean runRequest(IBillingRequest request) {
		return manager.run(request);
	}

	/**
	 * Requests that the given item be offered to the user for purchase. When
	 * the purchase succeeds (or is canceled) the {@link BillingReceiver}
	 * receives an intent with the action {@link Constants#ACTION_NOTIFY}. Returns
	 * false if there was an error trying to connect to Android Market.
	 * 
	 * @param productId
	 *            an identifier for the item being offered for purchase
	 * @param developerPayload
	 *            a payload that is associated with a given purchase, if null,
	 *            no payload is sent
	 * @return false if there was an error connecting to Android Market
	 */
	private boolean requestPurchase(BillingItem item) {
		return runRequest(new BillingRequest.RequestPurchase(item));
	}

	/**
	 * Requests transaction information for all managed items. Call this only
	 * when the application is first installed or after a database wipe. Do NOT
	 * call this every time the application starts up.
	 * 
	 * @return false if there was an error connecting to Android Market
	 */
	private boolean restoreTransactions() {
		return runRequest(new BillingRequest.RestoreTransactions());
	}

	/**
	 * Confirms receipt of a purchase state change. Each {@code notifyId} is an
	 * opaque identifier that came from the server. This method sends those
	 * identifiers back to the MarketBillingService, which ACKs them to the
	 * server. Returns false if there was an error trying to connect to the
	 * MarketBillingService.
	 * 
	 * @param startId
	 *            an identifier for the invocation instance of this service
	 * @param notifyIds
	 *            a list of opaque identifiers associated with purchase state
	 *            changes.
	 * @return false if there was an error connecting to Market
	 */
	private boolean confirmNotifications(int startId, String[] notifyIds) {
		return runRequest(new BillingRequest.ConfirmNotifications(startId, notifyIds));
	}

	/**
	 * Gets the purchase information. This message includes a list of
	 * notification IDs sent to us by Android Market, which we include in our
	 * request. The server responds with the purchase information, encoded as a
	 * JSON string, and sends that to the {@link BillingReceiver} in an intent
	 * with the action {@link Constants#ACTION_PURCHASE_STATE_CHANGED}. Returns
	 * false if there was an error trying to connect to the
	 * MarketBillingService.
	 * 
	 * @param startId
	 *            an identifier for the invocation instance of this service
	 * @param notifyIds
	 *            a list of opaque identifiers associated with purchase state
	 *            changes
	 * @return false if there was an error connecting to Android Market
	 */
	private boolean getPurchaseInformation(int startId, String[] notifyIds) {
		return runRequest(new BillingRequest.GetPurchaseInformation(startId, notifyIds));
	}

	/**
	 * Verifies that the data was signed with the given signature, and calls
	 * {@link ResponseHandler#purchaseResponse(Context, PurchaseState, String, String, long)}
	 * for each verified purchase.
	 * 
	 * @param startId
	 *            an identifier for the invocation instance of this service
	 * @param signedData
	 *            the signed JSON string (signed, not encrypted)
	 * @param signature
	 *            the signature for the data, signed with the private key
	 */
	private void purchaseStateChanged(int startId, String signedData,
			String signature) {
		ArrayList<Security.VerifiedPurchase> purchases;
		IBillingSecurity billingSecurity = new IBillingSecurity() {

			public String getMarketPublicKey() {
				return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqCFKfVkQAkmH03QHeBGK6LfQ/PVT6tzHLM7FcpCdlCFPx7bsaUIpyl//kRD4y2bkfUxZHhu7e08HoRgfxTrCzaL2a7aM6Jz+QoyImbnCly0zWKhdBO6XCYjpTtYHXtvpfN6oFvChMWNVPqTQgduYVVGdKg5A3gZUkkYZu+8qyvBOkd3rIS96SRbEVONFXRdfvBCDXopRjytSv1R5NJa3WYyEYQuNf2prOEgGQAaZVUYsjD1GwORBHl54iB/Idp0hi3x+T+c8/iW9tNRMmq06pll9nqmi33R2h4nRzwINKbpgF+bBWuFxhjj/SG1QlCQZe7yozDdjzUoJtFDOvOv64QIDAQAB";
			}
		};

		purchases = Security.verifyPurchase(billingSecurity, signedData,
				signature);
		if (purchases == null) {
			return;
		}

		ArrayList<String> notifyList = new ArrayList<String>();
		for (VerifiedPurchase vp : purchases) {
			if (vp.notificationId != null) {
				notifyList.add(vp.notificationId);
			}
			ResponseHandler.purchaseResponse(this, vp.purchaseState,
					vp.productId, vp.orderId, vp.purchaseTime,
					vp.developerPayload);
		}
		if (!notifyList.isEmpty()) {
			String[] notifyIds = notifyList.toArray(new String[notifyList
					.size()]);
			confirmNotifications(startId, notifyIds);
		}
	}

	/**
	 * This is called when we receive a response code from Android Market for a
	 * request that we made. This is used for reporting various errors and for
	 * acknowledging that an order was sent to the server. This is NOT used for
	 * any purchase state changes. All purchase state changes are received in
	 * the {@link BillingReceiver} and passed to this service, where they are
	 * handled in {@link #purchaseStateChanged(int, String, String)}.
	 * 
	 * @param requestId
	 *            a number that identifies a request, assigned at the time the
	 *            request was made to Android Market
	 * @param responseCode
	 *            a response code from Android Market to indicate the state of
	 *            the request
	 */
	private void checkResponseCode(long requestId, ResponseCode responseCode) {
		manager.acceptResponse(requestId, responseCode);
	}

	/**
	 * Unbinds from the MarketBillingService. Call this when the application
	 * terminates to avoid leaking a ServiceConnection.
	 */
	private void unbind() {
		manager.unbind();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void onDestroy() {
		super.onDestroy();
		this.unbind();
		Log.w(TAG, "Closing BillingService");
		stopForeground();
	}
	
	private void startForeground(String action) {
		if (foreground == null) {
			foreground = new ServiceForegrounder(this, 1);
		}
		
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				MarketBillingTestActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		foreground.startForeground(R.drawable.ic_launcher, "Billing", action,
				action, pi);
	}
	
	private void stopForeground() {
		foreground.stopForeground();
	}
	
	static void checkBillingSupported(Context context) {
		final Intent intent = getAction(context, Constants.ACTION_METHOD_CHECK_BILLING_SUPPORTED);
		callAction(context, intent);
	}
	
	static void requestPurchase(Context context, BillingItem item) {
		final Intent intent = getAction(context, Constants.ACTION_METHOD_REQUEST_PURCHASE, item);
		callAction(context, intent);
	}
	
	static void restoreTransactions(Context context) {
		final Intent intent = getAction(context, Constants.ACTION_METHOD_RESTORE_TRANSACTIONS);
		callAction(context, intent);
	}
	
	private static void callAction(Context context, Intent intent) {
		context.startService(intent);
	}
	
	private static Intent getAction(Context context, String action) {
		final Intent intent = new Intent(action);
		intent.setClass(context, BillingService.class);
		return intent;
	}
	
	private static Intent getAction(Context context, String action, Parcelable parameter) {
		final Intent intent = new Intent(action);
		intent.setClass(context, BillingService.class);
		intent.putExtra("parameter", parameter);
		return intent;
	}
}
