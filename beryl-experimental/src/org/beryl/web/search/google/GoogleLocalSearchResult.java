package org.beryl.web.search.google;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * http://code.google.com/apis/ajaxsearch/documentation/reference.html#_intro_fonje
 */
public class GoogleLocalSearchResult extends GoogleSearchResult
{
	protected GoogleLocalSearchResult(JSONObject sr)
	{
		try
		{
			inflateResponse(sr);
		}
		catch (JSONException e)
		{
		}
	}

	/** Supplies the title for the result. In some cases, the title and the streetAddress are the same. This typically occurs when the search term is a street address such as 1231 Lisa Lane, Los Altos, CA. */
	public String Title;
	
	/** Supplies the title, but unlike .title, this property is stripped of html markup (e.g., <b>, <i>, etc.). */
	public String TitleNoFormatting;

	/** Supplies the latitude value of the result. This may be used to construct a GPoint using the following code snippet:
	 * p = new GPoint(parseFloat(result.lng), parseFloat(result.lat)); */
	public double Latitude;
	
	/** Supplies the longitude value of the result. This may be used to construct a GPoint using the following code snippet:
	 * p = new GPoint(parseFloat(result.lng), parseFloat(result.lat)); */
	public double Longitude;
	
	/** Supplies the street address and number for the given result. Note: In some cases, this property may be set to "" if the result has no known street address. */
	public String StreetAddress;
	
	/** Supplies the city name for the result. Note: In some cases, this property may be set to "". */
	public String City;
	
	/** Supplies a region name for the result (e.g., in the U.S., this is typically a state abbreviation, in other regions it might be a province, etc.)
	 * Note: In some cases, this property may be set to "". */
	public String Region;
	
	/** Supplies a country name for the result. Note: In some cases, this property may be set to "". */
	public String Country;

	/** Supplies a url to a Google Maps Details page associated with the search result. */
	public String Url;
	
	
	/** Supplies an array of phone number objects where each object contains a .type property and a .number property.
	 * The value of the .type property can be one of "main", "fax", "mobile", "data", or "". */
	public ArrayList<PhoneNumber> PhoneNumbers = new ArrayList<PhoneNumber>();
	
	/** Supplies an array consisting of the mailing address lines for this result,
	 * for instance: ["1600 Amphitheatre Pky", "Mountain View, CA 94043"] or ["Via del Corso, 330", "00186 Roma (RM), Italy"].
	 * To correctly render an address associated with a result, either use the .html property of the result directly or iterate through this array and display each addressLine in turn. */
	public ArrayList<String> AddressLines = new ArrayList<String>();
	
	/** Supplies a url that can be used to provide driving directions from the center of the set of search results to this search result.
	 * Note, in some cases this property may be missing or null.
	 * Always wrap access within a a test of if (result.ddUrl && result.ddUrl != null).
	 */
	public String DrivingDirectionsUrl;
	
	/** Supplies a url that can be used to provide driving directions from a user specified location to this search result.
	 * Note, in some cases this property may be missing or null. Always wrap access within a a test of if (result.ddUrlToHere && result.ddUrlToHere != null)
	 */
	public String DrivingDirectionToUrl;
	
	/** Supplies a url that can be used to provide driving directions from this search result to a user specified location.
	 * Note, in some cases this property may be missing or null.
	 * Always wrap access within a a test of if (result.ddUrlFromHere && result.ddUrlFromHere != null).
	 */
	public String DrivingDirectionFromUrl;
	
	/** Supplies a url to a static map image representation of the current result.
	 * The image is 150px wide by 100px tall with a single marker representing the current location.
	 * Expected usage is to hyperlink this image using the url property.
	 * The image may be resized using google.search.LocalSearch.resizeStaticMapUrl().
	 * The Static Map Tile sample demonstrates one way to use this property.
	 */
	public String StaticMapUrl;
	
	/** This property indicates the type of this result which can either be "local" in the case of a local business listing or geocode result, or "kml" in the case of a KML listing. */
	public String ListingType;
	
	/** For "kml" results, this property contains a content snippet associated with the KML result.
	 * For "local" results, this property is the empty string. */
	public String Content;
	
	/** Undocumented */
	public int Age;
	
	/** Undocumented */
	public int Accuracy;

	/** Undocumented */
	public String ViewportMode;

	@Override
	public void inflateResponse(JSONObject jo) throws JSONException
	{
		ViewportMode = this.jsonString("viewportmode", jo);
		ListingType = this.jsonString("listingType", jo);
		Latitude = this.jsonDouble("lat", jo);
		Longitude = this.jsonDouble("lng", jo);
		Accuracy = this.jsonInt("accuracy", jo);
		Title = this.jsonString("title", jo);
		TitleNoFormatting = this.jsonString("titleNoFormatting", jo);
		DrivingDirectionsUrl = this.jsonString("ddUrl", jo);
		DrivingDirectionToUrl = this.jsonString("ddUrlToHere", jo);
		DrivingDirectionFromUrl = this.jsonString("ddUrlFromHere", jo);
		StreetAddress = this.jsonString("streetAddress", jo);
		City = this.jsonString("city", jo);
		Region = this.jsonString("region", jo);
		Country = this.jsonString("country", jo);
		StaticMapUrl = this.jsonString("staticMapUrl", jo);
		Url = this.jsonString("url", jo);
		Content = this.jsonString("content", jo);
		Age = this.jsonInt("maxAge", jo);
		
		JSONArray array;
		int len;
		int i;
		if(jo.has("phoneNumbers"))
		{
			JSONObject jpn;
			array = jo.getJSONArray("phoneNumbers");
			len = array.length();
			PhoneNumber pn;
			for(i = 0; i < len; i++)
			{
				jpn = (JSONObject) array.get(i);
				pn = new PhoneNumber(jsonString("type", jpn), jsonString("number", jpn));
				this.PhoneNumbers.add(pn);
			}
		}
		
		if(jo.has("addressLines"))
		{
			array = jo.getJSONArray("addressLines");
			len = array.length();
			for(i = 0; i < len; i++)
			{
				this.AddressLines.add(array.get(i).toString());
			}
		}
	}

}
