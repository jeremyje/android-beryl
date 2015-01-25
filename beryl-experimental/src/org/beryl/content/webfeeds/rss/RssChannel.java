package org.beryl.content.webfeeds.rss;

import java.util.ArrayList;


public class RssChannel
{
	public String title = null;
	public String link = null;
	public String description = null;
	public String language = null;
	public String copyright = null;
	public String managingEditor = null;
	public String webMaster = null;
	public String pubDate = null;
	public String category = null;
	public String generator = null;
	public String docs = null;
	public int ttl;
	public RssImage image = null;
	public String rating = null;
	public String lastBuildDate = null;
	public ArrayList<RssItem> items = new ArrayList<RssItem>();
}
