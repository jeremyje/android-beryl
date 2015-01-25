package org.beryl.web.url;

/** Interface for URL Shortening providers.
 * URL Shortening services take very long URLs and converts them to an auto-redirecting short URL appropriate for posting data on sites like twitter.
 * Requires: android.permission.INTERNET */
public interface IUrlShortener {
	
	/** Converts a long URL into a shortened version. */
	String shorten(String url);
	
	/** Converts a shortened URL into a longer version.
	 * Not all providers support this and may throw UnsupportedOperationException.
	 * If the the URL was not shorted by this service in particular an IllegalArgumentException is thrown.  */;
	String expand(String shortUrl);
	
	/** Tests whether this provider was used to shorten a URL. */
	boolean isUrlShortenedByProvider(String shortUrl);
	
	/** Gets the name of the URL Shortening service. */
	String getName();
	
	/** Gets the web address of the URL Shortening service. */
	String getUrl();
}
