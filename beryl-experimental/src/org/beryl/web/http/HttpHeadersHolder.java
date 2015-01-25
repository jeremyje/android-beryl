package org.beryl.web.http;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

/** An Http ResponseHandler that only gets the header information from the HttpResponse. */
public class HttpHeadersHolder<T> implements ResponseHandler<T> {

	protected HashMap<String, String> _headers = new HashMap<String, String>();
	
	public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		final Header[] headers = response.getAllHeaders();
		for(Header header : headers) {
			_headers.put(header.getName(), header.getValue());
		}
		
		return null;
	}
	
	public boolean hasHeaders() {
		return ! _headers.isEmpty();
	}
	
	public boolean contains(String name) {
		return _headers.keySet().contains(name);
	}
	
	public String get(String name) {
		return _headers.get(name);
	}
}
