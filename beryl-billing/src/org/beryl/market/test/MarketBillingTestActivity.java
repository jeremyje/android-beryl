package org.beryl.market.test;

import org.beryl.billing.AbstractBillingCallback;
import org.beryl.billing.InAppBilling;
import org.beryl.billing.DebugLogBillingCallback;

import com.futonredemption.example.dungeons.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MarketBillingTestActivity extends Activity {
	
	private InAppBilling billing;
	private final MyBillingCallback mBillingCallback = new MyBillingCallback();
	class MyBillingCallback extends AbstractBillingCallback {

		public MyBillingCallback() {
			super(new DebugLogBillingCallback());
		}
		
		@Override
		protected void onBillingSupported(final boolean supported) {
			billingStateTextView.setText("Billing Supported= " + supported);
			buyButton.setEnabled(supported);
			restoreButton.setEnabled(supported);
		}
	}
	
	private String purchaseCode;
	private Button buyButton;
	private Button restoreButton;
	private TextView billingStateTextView;
	private Spinner selectCodeSpinner;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        billing = new InAppBilling(this);
        
        bindViews();
        setupViews();
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	billing.start(mBillingCallback, this);
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	billing.stop();
    }
    
    private void bindViews() {
		purchaseCode = getString(R.string.android_test_purchased);
		
		buyButton = (Button)findViewById(R.id.buy_button);
		restoreButton = (Button)findViewById(R.id.restore_button);
		billingStateTextView = (TextView)findViewById(R.id.billing_state);
        selectCodeSpinner = (Spinner) findViewById(R.id.purchase_code);
	}
	
	private void setupViews() {
		buyButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(final View v) {
				final EditText payloadText = (EditText) findViewById(R.id.developer_payload);
				final String payload = payloadText.getText().toString();
				billing.beginPurchase(purchaseCode, payload);
			}
		});
        
        restoreButton.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				billing.restoreTransactions();
			}
		});
        
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        		R.array.purchase_codes,
        		android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCodeSpinner.setAdapter(adapter);
        selectCodeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(final AdapterView<?> parent,
					final View view, final int pos, final long id) {
				purchaseCode = parent.getItemAtPosition(pos).toString();
			}

			public void onNothingSelected(final AdapterView<?> arg0) {
				purchaseCode = getString(R.string.android_test_purchased);
			}
		});
	}
}