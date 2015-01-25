package org.beryl.intents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public interface IActivityResultHandler {

	void prepareResult(Context context, Bundle requestParams, int resultCode, Intent data);
	void onResultCanceled();
	void onResultCompleted();
	void onResultCustomCode(int resultCode);
}
