package org.beryl.cache.policies;

/** Cache expiration policy based on Http Header information. */
public class HttpExpirationPolicy extends ExpirationPolicy {

	// References:
	// http://betterexplained.com/articles/how-to-optimize-your-site-with-http-caching/
	
	/** Parameter set of Http Header information to be used for determining of the cache's lifetime. */
	public static class HttpCacheHeaders {
		/** ETag */ String eTag;
		/** Last-Modified */ String lastModified;
		/** Expires */ String expires;
		/** Max-Age */ String maxAge;
		/** Cache-Control */ String cacheControl;
	}
}
