package uk.ac.ed.inf.powergrab;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        getMapNodeList("2019", "01", "01");
    }
    
    public static void getMapNodeList(String year, String month, String day) throws IOException {
    	month = month.length() == 1 ? "0" + month : month;
    	day = day.length() == 1 ? "0" + day : day;
    	String date = year + "/" + month + "/" + day;
    	String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/"
    						+ date + "/powergrabmap.geojson";
    	
    	URL mapUrl = new URL(mapString);
    	HttpURLConnection conn = (HttpURLConnection) mapUrl.openConnection();
    	
    	conn.setReadTimeout(10000);
    	conn.setConnectTimeout(15000);
    	conn.setRequestMethod("GET");
    	conn.setDoInput(true);
    	conn.connect();
    	
    	String mapSource = convertStreamToString(conn.getInputStream());
    	FeatureCollection fc = FeatureCollection.fromJson(mapSource);
    	
    	for (Feature feat : fc.features()) {
    		System.out.println(feat);
    	}
    }
    
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
}
