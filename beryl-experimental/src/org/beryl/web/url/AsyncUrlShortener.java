package org.beryl.web.url;

/** Convenience class for performing URL shortening operations in a background thread with callbacks. */
public class AsyncUrlShortener {

	public static interface Callback {
		void onUrlShorten(final String shortenedUrl);
		void onUrlExpand(final String expandedUrl);
		void onUrlShorteningError(final Exception e);
	}
	
	final private Callback _callback;
	private IUrlShortener _provider;
	
	public AsyncUrlShortener(Callback callback) {
		_callback = callback;
	}
	public void setProvider(IUrlShortener provider) {
		_provider = provider;
	}
	
	public void beginShortenUrl(String longUrl) {
		final Thread t = new Thread(new UrlShortener(longUrl, _callback, _provider));
		t.setName("URL Shorten Worker");
		t.start();
	}
	
	public void beginExpandUrl(String shortUrl) {
		final Thread t = new Thread(new UrlExpander(shortUrl, _callback, _provider));
		t.setName("URL Expand Worker");
		t.start();
	}
	
	static class UrlShortener implements Runnable {

		final String _longUrl;
		final Callback _callback;
		final IUrlShortener _provider;
		
		public UrlShortener(String longUrl, Callback callback, IUrlShortener provider) {
			_longUrl = longUrl;
			_callback = callback;
			_provider = provider;
		}
		
		public void run() {
			try {
				final String shortUrl = _provider.shorten(_longUrl);
				_callback.onUrlShorten(shortUrl);
			}
			catch(Exception e) {
				_callback.onUrlShorteningError(e);
			}
		}
	}
	
	static class UrlExpander implements Runnable {

		final String _shortUrl;
		final Callback _callback;
		final IUrlShortener _provider;
		
		public UrlExpander(String shortUrl, Callback callback, IUrlShortener provider) {
			_shortUrl = shortUrl;
			_callback = callback;
			_provider = provider;
		}
		
		public void run() {
			try {
				final String longUrl = _provider.expand(_shortUrl);
				_callback.onUrlExpand(longUrl);
			}
			catch(Exception e) {
				_callback.onUrlShorteningError(e);
			}
		}
	}
}
