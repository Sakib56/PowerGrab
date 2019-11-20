package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;

import com.mapbox.geojson.FeatureCollection;

public class StatelessDrone extends Drone {
	
	public StatelessDrone(Position pos) {
		super(pos);
	}

	public FeatureCollection play(ArrayList<Node> mapNodes) {
		ArrayList<Position> bestNextPos = new ArrayList<Position>(); // bestNextPos (needs maintained)
		
		for (Node node : mapNodes) { // loop through all nodes
			double fromCurrDist = this.currentPos.getL2Dist(node.pos);
			
			
			if (fromCurrDist <= this.currentPos.step + node.r) { //inside big circle
				
				for (Position pos : this.posChoices) {
					double fromNextPosDist = pos.getL2Dist(node.pos);
					if (fromNextPosDist <= node.r) { // in AOE of one of the nodes (inside big circle)
						bestNextPos.add(pos);
					}
				}
			}
		}
		return null;
	}
}
