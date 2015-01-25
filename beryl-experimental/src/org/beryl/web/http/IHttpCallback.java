package org.beryl.web.http;

import android.os.Bundle;

public interface IHttpCallback
{
	public void onResponseCompleted(String response, String baseUrl, Bundle params);
	public void onError(Exception e);
}
