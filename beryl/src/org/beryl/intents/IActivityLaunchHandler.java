package org.beryl.intents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public interface IActivityLaunchHandler {
	public Activity getActivity();
	void startActivity(Intent intent);
	void startActivityForResult(Intent intent, int requestCode);
	public void acceptResponseBundle(Bundle responseBundle);
}
