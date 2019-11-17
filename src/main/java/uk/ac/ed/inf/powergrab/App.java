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
    private static java.util.Scanner scanner;

	public static void main( String[] args ) throws IOException
    {
//    	d = new Drone("Stateless");
    	ArrayList<Node> mapNodes = getMapNodeList("2019", "01", "01");
		for (Node nodes : mapNodes) {
    		System.out.println(nodes);
    	}
    }
    
    public static ArrayList<Node> getMapNodeList(String year, String month, String day) throws IOException {
    	month = month.length() == 1 ? "0"+month : month;
    	day = day.length() == 1 ? "0"+day : day;
    	String date = year+"/"+month+"/"+day;
    	String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/"+date+"/powergrabmap.geojson";
    	
    	FeatureCollection fc = connectToInfServer(mapString);
    	
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
    
    public static FeatureCollection connectToInfServer(String mapString) throws IOException {
    	URL mapUrl = new URL(mapString);
    	HttpURLConnection conn = (HttpURLConnection) mapUrl.openConnection();
    	
    	conn.setReadTimeout(10000);
    	conn.setConnectTimeout(15000);
    	conn.setRequestMethod("GET");
    	conn.setDoInput(true);
    	conn.connect();
    	
    	String mapSource = convertStreamToString(conn.getInputStream());
    	return FeatureCollection.fromJson(mapSource);
    }
    
    static String convertStreamToString(java.io.InputStream is) {
        scanner = new java.util.Scanner(is);
		java.util.Scanner s = scanner.useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
}
