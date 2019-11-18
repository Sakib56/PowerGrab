package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mapbox.geojson.FeatureCollection;

public class Drone {
	public String state;
	
	public Drone(String state) {
		this.state = state;
	}

	public FeatureCollection play(ArrayList<Node> mapNodes) {
		Boolean isStateless = this.state.toLowerCase().contains("stateless");
		return isStateless ? randomWalkPath() : smartPath();
	}
	
	private FeatureCollection randomWalkPath() {
		Direction dir = new Direction(0);
//		ArrayList<Direction> dirChoices = new ArrayList<Direction>();
		
		List<Direction> dirChoices = Arrays.asList(dir.E, dir.ENE, dir.ESE, dir.N, dir.NE, dir.NNE, dir.NNW, dir.NW, dir.S);
		
		return null;
	}

	private FeatureCollection smartPath() {
		return null;
	}
}
