package org.beryl.web.url;

import org.beryl.web.http.HttpHeadersHolder;
import org.beryl.web.http.HttpRest;

/** Base class for URL Shortener providers that report results in JSON format. */
public abstract class AbstractUrlShortener implements IUrlShortener {

	
	private static final int REASONABLE_TIMEOUT = 10000;

	protected String executeGet(String requestUrl) {
		String response = null;
		
		try {
			response = HttpRest.executeGet(requestUrl, null, REASONABLE_TIMEOUT, null);
		} catch (Exception e) {
			throw new UrlShorteningException(UrlShorteningException.MESSAGE_NETWORK, e);
		}
		
		return response;
	}
	
	protected String getRedirectUrl(String requestUrl) {
		String redirectUrl = null;
		
		try {
			final HttpHeadersHolder<String> handler = new HttpHeadersHolder<String>();
			HttpRest.executeGet(requestUrl, null, REASONABLE_TIMEOUT, null, handler);
			
			redirectUrl = handler.get("Location");
		} catch (Exception e) {
			throw new UrlShorteningException(UrlShorteningException.MESSAGE_NETWORK, e);
		}
		
		return redirectUrl;
	}
}
