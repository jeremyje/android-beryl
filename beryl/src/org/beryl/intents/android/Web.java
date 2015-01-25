package org.beryl.intents.android;

import org.beryl.intents.IIntentBuilder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Web {

	public static class View implements IIntentBuilder {
		public String Url;

		public Intent getIntent() {
			final Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(Url));
			return intent;
		}

		public void prepareIntent(Context context) {
			// Do Nothing
		}

		public boolean isChoosable() {
			return false;
		}

		public CharSequence getChooserTitle() {
			return "View Website with";
		}

		public boolean isValid() {
			return Url != null;
		}
	}

	public static final Intent viewUrl(final String url) {
		View viewer = new View();
		viewer.Url = url;
		return viewer.getIntent();
	}
}
