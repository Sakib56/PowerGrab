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
		// map is created via connecting to power grab inf website using a specific date
		// an initial position is chosen for the drone
		// a new state(less/full) drone is created using the init pos defined
		// drone, d is made to play which carries out the appropriate algorithm
		// path is then printed to terminal
		
    	ArrayList<Node> mapNodes = getMapNodeList("2019", "01", "01");
    	
    	float totalCoins = 0;
    	for (Node n: mapNodes) {
    		if (n.weight>0) {
    			totalCoins += n.coins;
    		}
    	}
    	
    	Position initPos = mapNodes.get(0).pos;
    	StatelessDrone d = new StatelessDrone(initPos, (long) 0); 
    	// second argument of stateless drone is the seed for the random walk
    	// this is so that the random walk is reproducible
    	
//    	StatefulDrone d = new StatefulDrone(initPos);
    	d.play(mapNodes);
    	d.printPath();
    	
    	System.out.println("\n"+d.toString());
    	System.out.println(d.currentCoins+"/"+totalCoins+" = "+100*d.currentCoins/totalCoins+"%");
    	
    }
    
	// returns an arraylist of node objects (charging stations)
    private static ArrayList<Node> getMapNodeList(String year, String month, String day) throws IOException {
    	// checks if inputs are in the correct format (ISO) yyyy/mm/dd
    	month = month.length() == 1 ? "0"+month : month;
    	day = day.length() == 1 ? "0"+day : day;
    	String date = year+"/"+month+"/"+day;
    	// constructs the correct URL to use based on the date
    	String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/"+date+"/powergrabmap.geojson";
    	
    	// establishes connection from power grab map for specific date and returns fc
    	FeatureCollection fc = connectToInfServer(mapString);
    	
    	ArrayList<Node> mapNodes = new ArrayList<Node>();
    	for (Feature f : fc.features()) {
    		// fc's info is parsed (coins, power, type, location)
    		float coins = f.getProperty("coins").getAsFloat();
    		float power = f.getProperty("power").getAsFloat();
    		String type = f.getProperty("marker-symbol").toString();
    		Point p = (Point) f.geometry();
    		double lat = p.latitude();
    		double lon = p.longitude();
    		
    		// new node is created for each charging station and is added to an arraylist which is returned
    		Node n = new Node(coins, power, type, new Position(lat, lon));
    		mapNodes.add(n);
    	}
    	return mapNodes;
    }
    
    // connects to given link and returns a feature collection
    private static FeatureCollection connectToInfServer(String mapString) throws IOException {
    	// creates the url using the mapString passed in and opens a connection
    	URL mapUrl = new URL(mapString);
    	HttpURLConnection conn = (HttpURLConnection) mapUrl.openConnection();
    	
    	// timeouts are set, http request set to GET and connection is started
    	conn.setReadTimeout(10000);
    	conn.setConnectTimeout(15000);
    	conn.setRequestMethod("GET");
    	conn.setDoInput(true);
    	conn.connect();
    	
    	// input stream returned by the connection is converted into a string (json)
    	// json is then converted into a feature collection and returned
    	String mapSource = convertStreamToString(conn.getInputStream());
    	return FeatureCollection.fromJson(mapSource);
    }
    
    // converts an input stream to a string so that it can be interpreted as a json for fc
    static String convertStreamToString(java.io.InputStream is) {
        scanner = new java.util.Scanner(is);
		java.util.Scanner s = scanner.useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}