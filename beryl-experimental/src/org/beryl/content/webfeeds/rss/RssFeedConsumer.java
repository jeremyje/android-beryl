package org.beryl.content.webfeeds.rss;

import java.io.InputStream;

import org.beryl.content.webfeeds.AbstractXmlFeedConsumer;
import org.beryl.content.webfeeds.WebFeedException;
import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

public class RssFeedConsumer extends AbstractXmlFeedConsumer {

	protected RootElement assembleHandler(final IRssHolder holder)
	{
		RootElement root = new RootElement("rss");
		
		Element channel = root.getChild("channel");
		Element item = channel.getChild("item");
		Element image = channel.getChild("image");

		assembleChannelHandler(holder, channel);
		assembleItemHandler(holder, item);
		assmebleImageHandle(holder, image);

		return root;
	}
	
	protected void assembleChannelHandler(final IRssHolder holder, Element channel)
	{
		channel.getChild("title").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().title = body; }
        });
		channel.getChild("link").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().link = body; }
        });
		channel.getChild("description").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().description = body; }
        });
		channel.getChild("language").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().language = body; }
        });
		channel.getChild("copyright").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().copyright = body; }
        });
		channel.getChild("managingEditor").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().managingEditor = body; }
        });
		channel.getChild("webMaster").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().webMaster = body; }
        });
		channel.getChild("pubDate").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().pubDate = body; }
        });
		channel.getChild("category").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().category = body; }
        });
		channel.getChild("generator").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().generator = body; }
        });
		channel.getChild("docs").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().docs = body; }
        });
		channel.getChild("ttl").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { try { holder.getChannel().ttl = Integer.parseInt(body); } catch (Exception e) {} }
        });
		channel.getChild("rating").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().rating = body; }
        });
		channel.getChild("lastBuildDate").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getChannel().lastBuildDate = body; }
        });
	}
	
	protected void assembleItemHandler(final IRssHolder holder, Element item)
	{
		item.setEndElementListener(new EndElementListener(){
            public void end() {
                holder.sealItem();
            }
        });
		item.getChild("title").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getRssItem().title = body; }
        });
		item.getChild("link").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getRssItem().link = body; }
        });
		item.getChild("description").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getRssItem().description = body; }
        });
		item.getChild("author").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getRssItem().author = body; }
        });
		item.getChild("category").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getRssItem().category = body; }
        });
		item.getChild("comments").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getRssItem().comments = body; }
        });
		item.getChild("enclosure").setStartElementListener(new StartElementListener(){
			public void start(Attributes attributes) {
				holder.getRssItem().enclosure_url = attributes.getValue("url");
				try { holder.getRssItem().enclosure_length = Integer.parseInt(attributes.getValue("length")); } catch (Exception e) {}
				holder.getRssItem().enclosure_type = attributes.getValue("type");
			}
        });
		item.getChild("guid").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getRssItem().guid = body; }
        });
		item.getChild("guid").setStartElementListener(new StartElementListener(){
			public void start(Attributes attributes) {
				try { holder.getRssItem().guid_isPermaLink = Boolean.parseBoolean(attributes.getValue("isPermaLink")); } catch (Exception e) {}
			}
        });
		item.getChild("pubDate").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getRssItem().pubDate = body; }
        });
		item.getChild("source").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getRssItem().source = body; }
        });
	}
	
	protected void assmebleImageHandle(final IRssHolder holder, Element image)
	{
		image.setEndElementListener(new EndElementListener(){
            public void end() {
                holder.sealImage();
            }
        });
		
		image.getChild("url").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getImage().url = body; }
        });
		image.getChild("title").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getImage().title = body; }
        });
		image.getChild("link").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getImage().link = body; }
        });
		image.getChild("width").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { try { holder.getImage().width = Integer.parseInt(body); } catch (Exception e) {} }
        });
		image.getChild("height").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { try { holder.getImage().height = Integer.parseInt(body); } catch (Exception e) {} }
        });
		image.getChild("description").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) { holder.getImage().description = body; }
        });
	}

	protected IRssHolder _rssHolder;
	protected IRssHolder createRssHolder()
	{
		_rssHolder = new BasicRssHolder();
		return _rssHolder;
	}
	
	public IRssHolder getData()
	{
		return _rssHolder;
	}
	
	public boolean parseStream(InputStream is) throws WebFeedException
	{
		final IRssHolder holder = createRssHolder();
		RootElement root = assembleHandler(holder);
		
		try {
			Xml.parse(is, Xml.Encoding.UTF_8, root.getContentHandler());
		}
		catch(Exception e) {
			throw new WebFeedException(e);
		}
		
		if(holder.getChannel().title != null) {
			return true;
		}
		return false;
	}
}
