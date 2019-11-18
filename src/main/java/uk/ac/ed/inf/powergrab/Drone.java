package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import com.mapbox.geojson.Geometry;

public class Drone {
	public String state;
	private ArrayList<Node> mapNodes;
	
	public Drone(String state) {
		this.state = state;
	}

	public Geometry play(ArrayList<Node> mapNodes) {
		this.mapNodes = mapNodes;
		Boolean isStateless = this.state.toLowerCase().contains("stateless");
		return isStateless ? randomWalkPath() : smartPath();
	}
	
	private Geometry randomWalkPath() {
		return null;
	}

	private Geometry smartPath() {
		return null;
	}
}
