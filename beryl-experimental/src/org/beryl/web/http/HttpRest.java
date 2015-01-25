package org.beryl.web.http;

import java.util.ArrayList;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.net.Uri;
import android.os.Bundle;

public class HttpRest
{
	/*
	public static String createUri(final String base_uri, final Bundle params)
	{
		String result = base_uri;
		
		return result;
	}
	*/
	
	private static final int HTTP_COMMAND_GET = 1;
	private static final int HTTP_COMMAND_POST = 2;
	
	public static void beginExecuteGet(final String baseUrl, final String refererUrl, final IHttpCallback callback, final Bundle params)
	{
		Thread t = new Thread(new HttpWorker (baseUrl, refererUrl, callback, params, HTTP_COMMAND_GET));
		t.setName("HttpWorker - " + baseUrl);
		t.start();
	}
	
	public static void beginExecutePost(final String baseUrl, final String refererUrl, final IHttpCallback callback, final Bundle params)
	{
		Thread t = new Thread(new HttpWorker (baseUrl, refererUrl, callback, params, HTTP_COMMAND_POST));
		t.setName("HttpWorker - " + baseUrl);
		t.start();
	}
	
	static class HttpWorker implements Runnable
	{
		public static final int DEFAULT_TIMEOUT = 10000; // 10 seconds.
		private final IHttpCallback _callback;
		private final String _baseUrl;
		private final String _refererUrl;
		private int _timeout = DEFAULT_TIMEOUT;
		private final Bundle _params;
		private final int _httpCommand;
		
		
		public HttpWorker(String baseUrl, String refererUrl, IHttpCallback callback, Bundle params, int httpCommand)
		{
			_baseUrl = baseUrl;
			_refererUrl = refererUrl;
			_callback = callback;
			_params = params;
			_httpCommand = httpCommand;
		}
		
		public void setTimeout(int timeout)
		{
			_timeout = timeout;
		}

		public void run()
		{
			try
			{
				String response = null;
				switch(_httpCommand)
				{
					case HTTP_COMMAND_GET:
					{
						response = executeGet(_baseUrl, _refererUrl, _timeout, _params);
					}break;
					case HTTP_COMMAND_POST:
					{
						response = executePost(_baseUrl, _refererUrl, _timeout, _params);
					}break;
					default:
					{
						throw new IllegalArgumentException("Invalid HTTP command was requested.");
					}
				}
				
				_callback.onResponseCompleted(response, _baseUrl, _params);
			}
			catch(Exception e)
			{
				_callback.onError(e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static final String createUriFromGet(final String base_uri, final Bundle params)
	{
		Object temp;
		boolean first_element = true;
		StringBuilder sb = new StringBuilder();
		sb.append(base_uri);
		
		if(params != null)
		{
			final Set<String> keyset = params.keySet();
			ArrayList<Object> object_list;
			Object[] rawlist;
			StringBuilder sbi;
			
			for(String key : keyset)
			{
				temp = params.get(key);
	
				if(temp != null)
				{
					if(first_element)
					{
						sb.append("?");
						first_element = false;
					}
					else
					{
						sb.append("&");
					}
					
					sb.append(key);
					sb.append("=");
		
					if( temp instanceof String ||
						temp instanceof Integer ||
						temp instanceof Double ||
						temp instanceof Boolean)
					{
						sb.append(Uri.encode(temp.toString()));
					}
					else if(temp instanceof Object[])
					{
						sbi = new StringBuilder();
						rawlist = (Object[])temp;
						int rllen = rawlist.length;
						int rli;
						for(rli = 0; rli < rllen; rli++)
						{
							if(rli > 0)
							{
								sbi.append(",");
							}
							sbi.append(Uri.encode(rawlist[rli].toString()));
						}
					}
					else if(temp instanceof ArrayList<?>)
					{
						object_list = (ArrayList<Object>) temp;
						sbi = new StringBuilder();
						int sli;
						int sllen = object_list.size();
						for(sli = 0; sli < sllen; sli++)
						{
							if(sli > 0)
							{
								sbi.append("+");
							}
							sbi.append(Uri.encode(object_list.get(sli).toString()));
						}
						sb.append(sbi.toString());
					}
					else
					{
						String temps = temp.getClass().getName();
					}
				}
			}
		}
		return sb.toString();
	}
	
	private static final ArrayList<NameValuePair> bundleToNameValuePair(final Bundle params)
	{
		final ArrayList<NameValuePair> result = new ArrayList<NameValuePair>();
		Object temp;
		final Set<String> keyset = params.keySet();
		for(String key : keyset)
		{
			temp = params.get(key);
			
			if(temp == null)
			{
				result.add(new BasicNameValuePair(key, null));
			}
			else
			{
				result.add(new BasicNameValuePair(key, temp.toString()));
			}
		}
		
		return result;
	}
	
	public static String executePost (final String baseUrl, final String refererUrl, final int timeout, final Bundle params) throws Exception
	{
		final DefaultHttpClient client = new DefaultHttpClient();
		HttpParams httpparams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpparams, 1500);
		HttpConnectionParams.setSoTimeout(httpparams, 1500);
		client.setParams(httpparams);

		final HttpPost poster = new HttpPost(baseUrl);
		
		if (refererUrl != null)
			poster.setHeader("Referer", refererUrl);
		
		// Populate the message
		final ArrayList<NameValuePair> nvp = bundleToNameValuePair(params);
		poster.setEntity(new UrlEncodedFormEntity(nvp));				

		ResponseHandler<String> handler = new BasicResponseHandler();
		final String response = client.execute(poster, handler);
		
		return response;
	}
	
	public static String executeGet(final String baseUrl, final String refererUrl, final int timeout, final Bundle params) throws Exception
	{
		return executeGet(baseUrl, refererUrl, timeout, params, new BasicResponseHandler());
	}
	
	public static String executeGet(final String baseUrl, final String refererUrl, final int timeout, final Bundle params, ResponseHandler<String> handler) throws Exception
	{
		final String getUrl = createUriFromGet(baseUrl, params);

		String response = "";
		final DefaultHttpClient client = new DefaultHttpClient();
		HttpParams httpparams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpparams, timeout);
		HttpConnectionParams.setSoTimeout(httpparams, timeout);
		client.setParams(httpparams);
		
		HttpGet getter = new HttpGet(getUrl);
		
		if (refererUrl != null)
			getter.setHeader("Referer", refererUrl);
		
		response = client.execute(getter, handler);
		
		return response;
	}
}
