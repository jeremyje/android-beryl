package org.beryl.web.url;

import org.json.JSONObject;

import android.net.Uri;

/** URL Shortening provided by ur.ly */
public class UrlyUrlShortener extends AbstractUrlShortener {

	private static final String API_SHORTEN_URL = "http://ur.ly/new.json?href=%s";
	private static final String API_URL_PATTERN = "http://ur.ly/%s";
	public String shorten(String url) {
		
		String requestUrl = String.format(API_SHORTEN_URL, Uri.encode(url));
		String shortUrl = null;
		
		JSONObject json;
		try {
			String response = this.executeGet(requestUrl);
			json = new JSONObject(response);
			final String code = json.getString("code");
			shortUrl = String.format(API_URL_PATTERN, code);
		} catch (Exception e) {
			throw new UrlShorteningException(UrlShorteningException.MESSAGE_FORMAT, e);
		}
		
		return shortUrl;
	}

	public String expand(String shortUrl) {
		
		String requestUrl = shortUrl + ".json";
		String expandedUrl = null;
		
		JSONObject json;
		try {
			String response = this.executeGet(requestUrl);
			json = new JSONObject(response);
			expandedUrl = json.getString("href");
		} catch (Exception e) {
			throw new UrlShorteningException(UrlShorteningException.MESSAGE_FORMAT, e);
		}
		
		return expandedUrl;
	}

	public boolean isUrlShortenedByProvider(String shortUrl) {
		return shortUrl.contains("ur.ly/");
	}

	public String getName() {
		return "ur.ly";
	}

	public String getUrl() {
		return "http://ur.ly/";
	}
}
