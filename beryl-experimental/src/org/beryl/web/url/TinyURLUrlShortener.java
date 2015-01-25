package org.beryl.web.url;

import android.net.Uri;

/** URL Shortening provided by TinyURL. */
public class TinyURLUrlShortener extends AbstractUrlShortener {

	private static final String API_SHORTEN_URL = "http://tinyurl.com/api-create.php?url=%s";
	
	public String shorten(String url) {
		String requestUrl = String.format(API_SHORTEN_URL, Uri.encode(url));
		String response = this.executeGet(requestUrl);
		
		return response;
	}

	public String expand(String shortUrl) {
		if(! isUrlShortenedByProvider(shortUrl))
			throw new UrlShorteningException(UrlShorteningException.MESSAGE_NOTSUPPORTED, null);
		
		return this.getRedirectUrl(shortUrl);
	}

	public boolean isUrlShortenedByProvider(String shortUrl) {
		return shortUrl.contains("tinyurl.com/");
	}

	public String getName() {
		return "TinyURL";
	}

	public String getUrl() {
		return "http://www.tinyurl.com";
	}

}
