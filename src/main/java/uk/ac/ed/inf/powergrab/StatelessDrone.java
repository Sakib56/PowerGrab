package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;

import com.mapbox.geojson.FeatureCollection;

public class StatelessDrone extends Drone {
	
	public StatelessDrone(Position pos) {
		super(pos);
	}

	public FeatureCollection play(ArrayList<Node> mapNodes) {
		ArrayList<Position> bestNextPos = new ArrayList<Position>(); // best next pos to move to (needs maintained)
		
		for (Node node : mapNodes) { // loop through all nodes
			double fromCurrDist = this.currentPos.getL2Dist(node.pos); // calc dist from currentPos to each node, n
			if (fromCurrDist <= this.currentPos.step + node.r) { // inside big circle, n falls in threshold (step+roe)
				
				for (Position pos : this.posChoices) { // loop through all next possible pos's
					double fromNextPosDist = pos.getL2Dist(node.pos); // get dist from next pos to that node, n
					if (fromNextPosDist <= node.r) { // in AOE of one of the nodes (inside big circle)
						bestNextPos.add(pos); // add to next pos to move to (need maintained)
					}
				}
			}
		}
		return null;
	}
}
