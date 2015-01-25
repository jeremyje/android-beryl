package org.beryl.intents.android;

import java.util.ArrayList;

import org.beryl.intents.IIntentBuilder;

import android.content.Context;
import android.content.Intent;

public class Email {

	public static class Send implements IIntentBuilder {

		public final ArrayList<String> sendToEmailAddress = new ArrayList<String>();
		public String subject = null;
		public String body = null;

		public void prepareIntent(Context context) {
		}

		public Intent getIntent() {
			final Intent intent = new Intent(Intent.ACTION_SEND);
	    	intent.putExtra(Intent.EXTRA_EMAIL, (String[])sendToEmailAddress.toArray(new String[sendToEmailAddress.size()]));
	    	if(subject != null) intent.putExtra(Intent.EXTRA_SUBJECT, subject);
	    	if(body != null) intent.putExtra(Intent.EXTRA_TEXT, body);
	    	intent.setType("message/rfc822");

	    	return intent;
		}


		public boolean isChoosable() {
			return true;
		}

		public CharSequence getChooserTitle() {
			return "Send Email via...";
		}

		public boolean isValid() {
			return true;
		}
	}

	public static final Intent sendEmail(final String address, final String subject, final String body) {
		Send emailer = new Send();
		emailer.sendToEmailAddress.add(address);
		emailer.subject = subject;
		emailer.body = body;
		return emailer.getIntent();
	}
}
