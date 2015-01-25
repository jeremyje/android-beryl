package org.beryl.content.webfeeds;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class AbstractXmlFeedConsumer implements IWebFeedConsumer {

	protected String _url = null;
	
	public boolean consumeUrl(String url) throws WebFeedException {
		_url = url;
		InputStream is = openStream(url);
		return this.parseStream(is);
	}
	
	private static InputStream openStream(String url) throws WebFeedException {
		
		InputStream is = null;
		URL urlhandler = null;
		
		try {
			urlhandler = new URL(url);
		} catch (MalformedURLException e) {
			throw new WebFeedException(e);
		}
		finally {
			
		}
		
		try {
			is = urlhandler.openStream();
		} catch (IOException e) {
			throw new WebFeedException(e);
		}
		finally {
			
		}
		
		return is;
	}
	
	public String getSourceUrl() {
		return _url;
	}
	
	protected abstract boolean parseStream(final InputStream is) throws WebFeedException;
}
