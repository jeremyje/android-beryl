package org.beryl.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/** A labeled Intent that can be chosen from the IntentChooser activity. */
public class ChoosableIntent implements Parcelable {

	public static final int RUNAS_Activity = 0;
	public static final int RUNAS_Service = 1;
	public static final int RUNAS_Broadcast = 2;

	protected final int runIntentAs;
	protected final CharSequence label;
	protected final Intent intent;

	public ChoosableIntent(final CharSequence label, final Intent intent) {
		this.label = label;
		this.intent = intent;
		this.runIntentAs = RUNAS_Activity;
	}

	public ChoosableIntent(final CharSequence label, final Intent intent, final int runAs) {
		this.label = label;
		this.intent = intent;
		this.runIntentAs = runAs;
	}

	public ChoosableIntent(final Parcel in) {
		final Bundle bundle = in.readBundle();

		this.runIntentAs = bundle.getInt("runIntentAs");
		this.label = bundle.getCharSequence("label");
		this.intent = bundle.getParcelable("intent");
	}

	public boolean isValid() {
		return label != null && intent != null;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(final Parcel dest, final int flags) {
		final Bundle bundle = new Bundle();
		bundle.putInt("runIntentAs", this.runIntentAs);
		bundle.putCharSequence("label", this.label);
		bundle.putParcelable("intent", this.intent);
		dest.writeBundle(bundle);
	}

	public static final Parcelable.Creator<ChoosableIntent> CREATOR = new Parcelable.Creator<ChoosableIntent>() {
        public ChoosableIntent createFromParcel(final Parcel in) {
            return new ChoosableIntent(in);
        }

        public ChoosableIntent[] newArray(final int size) {
            return new ChoosableIntent[size];
        }
    };
}