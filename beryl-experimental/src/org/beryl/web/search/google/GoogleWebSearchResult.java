package org.beryl.web.search.google;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * http://code.google.com/apis/ajaxsearch/documentation/reference.html#_intro_fonje
 */
public class GoogleWebSearchResult extends GoogleSearchResult
{
	protected GoogleWebSearchResult(JSONObject sr)
	{
		try
		{
			inflateResponse(sr);
		}
		catch (JSONException e)
		{
		}
	}

	/** Supplies the raw URL of the result. */
	public String UnescapedUrl;
	
	/** Supplies an escaped version of the above URL. */
	public String Url;

	/** Supplies a shortened version of the URL associated with the result. Typically displayed in green, stripped of a protocol and path. */
	public String VisibleUrl;
	
	/** Supplies the title value of the result. */
	public String Title;
	
	/** Supplies the title, but unlike .title, this property is stripped of html markup (e.g., <b>, <i>, etc.). */
	public String TitleNoFormatting;
	
	/** Supplies a brief snippet of information from the page associated with the search result. */
	public String Content;
	
	/** Supplies a url to Google's cached version of the page responsible for producing this result.
	 * This property may be null indicating that there is no cache, and it might be out of date in cases where the search result has been saved and in the mean time, the cache has gone stale.
	 * For best results, this property should not be persisted. */
	public String CacheUrl;
	
	@Override
	public void inflateResponse(JSONObject jo) throws JSONException
	{
		UnescapedUrl = this.jsonString("unescapedUrl", jo);
		Url = this.jsonString("url", jo);
		VisibleUrl = this.jsonString("visibleUrl", jo);
		Title = this.jsonString("title", jo);
		TitleNoFormatting = this.jsonString("titleNoFormatting", jo);
		Content = this.jsonString("content", jo);
		CacheUrl = this.jsonString("cacheUrl", jo);
	}

}
