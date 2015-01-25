package org.beryl.intents;

import android.content.Context;
import android.content.Intent;

public interface IIntentBuilder {

	/** Builds the intent using the parameters assigned to the object.
	 * NOTE: This operation can be expensive and should be performed on a background thread. This must be called before getIntent(). */
	void prepareIntent(Context context);

	/** Returns the Intent that represents the request. */
	Intent getIntent();
	boolean isValid();

	boolean isChoosable();
	CharSequence getChooserTitle();
}
