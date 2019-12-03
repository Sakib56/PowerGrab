package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatefulDrone extends Drone {

	public StatefulDrone(Position initPos) {
		super(initPos);
	}

	public void play(ArrayList<Node> mapNodes) {
		Map<Double, Node> DistNodeMap = getUnsortedBestNextPos(mapNodes);
		ArrayList<Node> closestOrdering = getClosestOrdering(DistNodeMap);			
		
		for (Node node : closestOrdering) {
			moveStarightTo(node);
		}
	}	
	
	public ArrayList<Node> getClosestOrdering(Map<Double, Node> distNodeMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Double, Node> getUnsortedBestNextPos(ArrayList<Node> mapNodes) {
		Map<Double, Node> unsortedBestNextPos = new HashMap<Double, Node>();	
		for (Node node : mapNodes) {
			if (!node.used) {
				double fromCurrDist = this.currentPos.getL2Dist(node.pos); 			
				Boolean nInsideGreaterCircle = fromCurrDist <= this.currentPos.getStep()+node.getRadius();
				
				if (nInsideGreaterCircle) { 										
					for (Position pos : this.posChoices) { 							
						double fromNextPosDist = pos.getL2Dist(node.pos); 			
						
						if (fromNextPosDist <= node.getRadius()) { 					
							Node reachableNode = new Node((float) node.coins, (float) node.power, node.type, node.pos, pos);
							unsortedBestNextPos.put(fromNextPosDist, reachableNode); 							
						}
					}
				}
			}
		}
		return unsortedBestNextPos;
	}
}
