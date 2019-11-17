package uk.ac.ed.inf.powergrab;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	ArrayList<Node> mapNodes = getMapNodeList("2019", "01", "01");
		for (Node nodes : mapNodes) {
    		System.out.println(nodes);
    	}
    }
    
    public static ArrayList<Node> getMapNodeList(String year, String month, String day) throws IOException {
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
    	
    	ArrayList<Node> mapNodes = new ArrayList<Node>();
    	for (Feature f : fc.features()) {
    		float coins = f.getProperty("coins").getAsFloat();
    		float power = f.getProperty("power").getAsFloat();
    		String type = f.getProperty("marker-symbol").toString();
    		Point p = (Point) f.geometry();
    		double lat = p.latitude();
    		double lon = p.longitude();
    		
    		Node n = new Node(coins, power, type, new Position(lat, lon));
    		mapNodes.add(n);
    	}
    	return mapNodes;
    }
    
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
}
