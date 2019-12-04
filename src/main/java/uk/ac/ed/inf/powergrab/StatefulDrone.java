package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class StatefulDrone extends Drone {
	private Map<Double, Node> distNodeMap;
	private ArrayList<Node> mapNodes;

	public StatefulDrone(Position initPos) {
		super(initPos);
	}

	// Play method carries out the algorithm
	// While the Drone is alive, find closest green node, and go in a straight line to it, repeat
	public void play(ArrayList<Node> mapNodes) {
		this.mapNodes = mapNodes;
		
		// While the drone is alive...
		while (isAlive()) {
			// Create a map (key, value) pairs where the key is the 
			// distance from currentPos to a node and value is the node object
			// This map is then sorted by distances (key), ascending order
			// Then the closest green node is chosen as a target
			// Drone will then move to this target in a straight line (ish) 
			
			Map<Double, Node> unsortedDistNodes = getUnsortedDistNodes(); // create unsorted distance-node map
			this.distNodeMap = sortDistNodes(unsortedDistNodes);		  // sort this map, smallest dist first
			Node closestGoodNode = getClosestOrdering();				  // get closest green charging station from this map
			
			// if the closest green node is null
			if (closestGoodNode == null) {
				// this means we have visited all the green nodes and drone should stop taking steps
				break;
			}
			
			// drone moves straight to the closest green node, g
			// g is no longer considered (removed from mapNodes)
			moveStarightTo(closestGoodNode);
			this.mapNodes.removeIf(n -> n.pos.isTheSame(closestGoodNode.pos));
		}
	}	

	// Return a map (key-value pairs) where the key is the distance to a node and the value is the node itself
	public Map<Double, Node> getUnsortedDistNodes() {
		// Create a new map obj (double for dist, Node for charging stations)
		Map<Double, Node> unsortedDistNodes = new HashMap<Double, Node>();
		// For each node, n in the map...
		for (Node node : this.mapNodes) {
			// if n has not already been used...
			if (!node.used) {
				// get distance, d from current position to n's position
				// then add d and n to map
				double fromCurrDist = this.currentPos.getL2Dist(node.pos); 		
				unsortedDistNodes.put(fromCurrDist, node);
			}
		}
		return unsortedDistNodes;
	}
	
	// Map<Double, Node> unsortedDistNodes is sorted by key (distance,
	// from a node within radius of effect and next possible move)
	public Map<Double, Node> sortDistNodes(Map<Double, Node> unsortedDistNodes) {
        Map<Double, Node> distNodes = new TreeMap<Double, Node>(new Comparator<Double>()
        { @Override
        	// compare is overridden so that sort is ascending order
            public int compare(Double i, Double j) {
                return i.compareTo(j);
            }
        });
        distNodes.putAll(unsortedDistNodes); // sort
        return distNodes;
	}
	
	public Node getClosestOrdering() {
		double maxWeight = -999;
		Node bestNode = null;
		
		for (Entry<Double, Node> entry : this.distNodeMap.entrySet()) {
			Node node = entry.getValue();	
			if (node.weight > maxWeight) {
				maxWeight = node.weight;
				bestNode = node;
			}
		}
		return bestNode;
	}
	
	public void moveStarightTo(Node node) {
		double distToNode = this.currentPos.getL2Dist(node.pos);
		for (double i=0; i <= Math.ceil(distToNode/this.currentPos.getStep()); i++) {
			double angle = this.currentPos.getAngleBetween(node.pos);
			Direction dirToMoveIn = new Direction().snapDir(angle);
			Position posToMoveIn = this.currentPos.nextPosition(dirToMoveIn);
			
			moveTo(posToMoveIn);
		}
	}
	
	public void moveTo(Position pos) {
		if (isAlive() && pos.inPlayArea() && this.movesMadeSoFar.size() <= this.maxMovesAllowed) {
			this.currentPower += powerConsump;
			this.currentPos = pos;
			this.movesMadeSoFar.add(pos);
			
			Map<Double, Node> unsortedDistNodes = getUnsortedDistNodes();
			this.distNodeMap = sortDistNodes(unsortedDistNodes);
			
			for (Entry<Double, Node> entry : this.distNodeMap.entrySet()) {
				double dist = entry.getKey();
				Node node = entry.getValue();
				if (dist <= node.getRadius()) {
					use(node);
					break;
				}
			}
		}
		this.posChoices = getNextMoves();
	}
}
