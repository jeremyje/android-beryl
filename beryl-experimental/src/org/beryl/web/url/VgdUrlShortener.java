package org.beryl.web.url;

import org.json.JSONObject;

import android.net.Uri;

/** URL Shortening provided by v.gd */
public class VgdUrlShortener extends AbstractUrlShortener {

	private static final String API_SHORTEN_URL = "http://v.gd/api.php?format=json&url=%s";
	private static final String API_SHORTEN_URL_WITH_CUSTOM_CODE = "http://v.gd/api.php?format=json&shorturl=%s&url=%s";
	public String shorten(String url) {
		String requestUrl = String.format(API_SHORTEN_URL, Uri.encode(url));
		String response = this.executeGet(requestUrl);
		
		return response;
	}

	public String shorten(String url, String code) {
		String requestUrl = String.format(API_SHORTEN_URL_WITH_CUSTOM_CODE, code, Uri.encode(url));
		String shortUrl = null;
		
		JSONObject json;
		try {
			String response = this.executeGet(requestUrl);
			json = new JSONObject(response);
			if(json.has("errorcode")) {
				StringBuilder sb = new StringBuilder();
				sb.append(UrlShorteningException.MESSAGE_FORMAT);
				sb.append("   ");
				sb.append(json.getString("errorcode"));
				sb.append(" - ");
				sb.append(json.getString("errormessage"));
				throw new UrlShorteningException(sb.toString(), null);
			}
			
			shortUrl = json.getString("shorturl");
		} catch (Exception e) {
			throw new UrlShorteningException(UrlShorteningException.MESSAGE_FORMAT, e);
		}
		
		return shortUrl;
	}
	
	public String expand(String shortUrl) {
		if(! isUrlShortenedByProvider(shortUrl))
			throw new UrlShorteningException(UrlShorteningException.MESSAGE_NOTSUPPORTED, null);
		
		return this.getRedirectUrl(shortUrl);
	}

	public boolean isUrlShortenedByProvider(String shortUrl) {
		return shortUrl.contains("v.gd/");
	}

	public String getName() {
		return "v.gd";
	}

	public String getUrl() {
		return "http://v.gd";
	}

}
