package org.beryl.web.cache;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.beryl.diagnostics.ExceptionReporter;

public class FileDownloader {

	/** Downloads a remote file in the form of a ByteArray. */
	public static byte[] getAsByteArray(String url) {
		byte[] result = null;
		
		try {
			final DefaultHttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if(entity != null) {
					result = EntityUtils.toByteArray(entity);
				}
			}
		} catch(Exception e) {
			ExceptionReporter.report(e);
		}
		
		return result;
	}

	/** Downloads a remote file in the form of an InputStream. */
	public static InputStream getAsInputStream(String url) {
		InputStream result = null;
		try
		{
			URL link = new URL(url);
			URLConnection con = link.openConnection();
			
			result = con.getInputStream();
			
		} catch(Exception e) {
			result = null;
			ExceptionReporter.report(e);
		}
		return result;
	}
}
