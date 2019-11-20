package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.mapbox.geojson.FeatureCollection;

public class StatelessDrone extends Drone {
	
	public StatelessDrone(Position initPos) {
		super(initPos);
	}

	public FeatureCollection play(ArrayList<Node> mapNodes) {
		Map<Double, Node> unsortedBestNextPos = new HashMap<Double, Node>();	// best next position to move to (needs maintained)
		
		for (Node node : mapNodes) { 											// loop through all nodes
			double fromCurrDist = this.currentPos.getL2Dist(node.pos); 			// get distance from currentPos to each node, n
			Boolean nInsideGreaterCircle = fromCurrDist <= this.currentPos.getStep()+node.getRadius();
			
			if (nInsideGreaterCircle) { 										// inside big circle, n falls in threshold (step + r)
				for (Position pos : this.posChoices) { 							// loop through all next possible positions (for each direction)
					double fromNextPosDist = pos.getL2Dist(node.pos); 			// get distance from next position to that node, n
					
					if (fromNextPosDist <= node.getRadius()) { 					// if in AOE of one of the nodes (inside smaller circle)
						Node reachableNode = new Node((float) node.coins, (float) node.power, node.type, node.pos, pos);
						unsortedBestNextPos.put(fromNextPosDist, reachableNode); 		// add to next position to move to (need maintained)					
					} else {
						Node reachableNode = new Node((float) node.coins, (float) node.power, node.type, node.pos, pos);
						unsortedBestNextPos.put(fromNextPosDist, reachableNode); 		// add to next position to move to (need maintained)
					}
				}
			}
		}
		
		// Map is sorted by key (distance)
        Map<Double, Node> bestNextPos = new TreeMap<Double, Node>(new Comparator<Double>()
        { @Override
            public int compare(Double i, Double j) {
                return i.compareTo(j);
            }
        });
        bestNextPos.putAll(unsortedBestNextPos); 
        
		for (Entry<Double, Node> entry : bestNextPos.entrySet()) {
            double key = (double) entry.getKey();
            Node value = entry.getValue();
            System.out.println(value.toString()+" has dist:"+key+" from "+value.tempFromPos);
        }
        
		return null;
	}
}
