package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

// sub class of drone (super class)
public class StatefulDrone extends Drone {
	private Map<Double, Node> distNodeMap;
	private ArrayList<Node> mapNodes;

	// stateful drone constructer
	public StatefulDrone(Position initPos) {
		super(initPos);
	}

	// play method carries out the algorithm
	// while the drone is alive, find closest green node, and go in a straight line to it, repeat
	public void play(ArrayList<Node> mapNodes) {
		this.mapNodes = mapNodes;
		
		// while the drone is alive...
		while (isAlive() && this.movesMadeSoFar.size() < this.maxMovesAllowed) {
			// create a map (key, value pairs) where the key is the 
			// distance from currentPos to a node and value is the node object
			// this map is then sorted by key (distances) in ascending order
			// then the closest green node is chosen as a target
			// drone will then move to this target in a straight-ish line
						
			// unlike the stateless drone, this distance-node hashmap contains all nodes in the map
			// where as stateless has a distance-node map only contains nodes in range within one step
			Map<Double, Node> unsortedDistNodes = getUnsortedDistNodes(); // create unsorted distance-node map
			this.distNodeMap = sortDistNodes(unsortedDistNodes);		  // sort this map using key, smallest dist first...
			Node closestGoodNode = getClosestGreenNode();				  // get closest green charging station from this map
			
			// if the closest green node is null
			if (closestGoodNode == null) {
				// this means we have visited all the green nodes and drone should stop taking steps 
				// (no point in moving if there's no more usable green nodes)
				break;
			}
			
			// drone moves straight to the closest green node, g
			// g is no longer considered (removed from mapNodes)
			moveStarightTo(closestGoodNode);
		}
	}	

	// return a map (key-value pairs) where the key is the distance to a node and the value is the node itself
	public Map<Double, Node> getUnsortedDistNodes() {
		// create a new map object (double for dist, node for charging stations)
		Map<Double, Node> unsortedDistNodes = new HashMap<Double, Node>();
		
		// for each node, n in mapNodes (list of all the nodes)...
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
        distNodes.putAll(unsortedDistNodes); // sort occurs when unsorted map is put into new map (since new comparator)
        return distNodes;
	}
	
	// returns the the closest green charging station by doing weight comparisons
	public Node getClosestGreenNode() {
		// linear search through the map to find green node, since map is already sorted by key (distance)
		// we can be sure that the first node with weight>0 (green) is the closest green node
		Node bestNode = null;
		
		// for every node, n in the map...
		for (Entry<Double, Node> entry : this.distNodeMap.entrySet()) {
			Node node = entry.getValue();
			// check if n is green, break out of loop and return n (as it's the closest green charging station) 
			if (node.weight > 0) {
				bestNode = node;
				break;
			}
		}
		return bestNode;
	}
	
	// makes drone move in a straight-ish line to a node
	public void moveStarightTo(Node node) {
		// calculate number of steps needed to reach that node, ceil(dist/step_size)
		double distToNode = this.currentPos.getL2Dist(node.pos);
		
		for (double i=0; i <= Math.ceil(distToNode/this.currentPos.getStep()); i++) {
			// get the angle from current position to node's position, from east (x-axis)
			// snap the angle to 1 of 16 allowable directions
			// move in that direction (for number of steps needed)
			double angle = this.currentPos.getAngleBetween(node.pos);
			Direction dirToMoveIn = new Direction().snapDir(angle);
			Position posToMoveIn = this.currentPos.nextPosition(dirToMoveIn);
			moveTo(posToMoveIn);
		}
	}
	
	// makes drone move to a specific position
	public void moveTo(Position pos) {
		// move only occurs if drone is still alive and the position 
		// to be moved to, pos is in the playable area

		if (isAlive() && pos.inPlayArea()) {
			// power reduced by the power consumption (step cost)
			// current position is updated to new pos
			this.currentPower += powerConsump;
			this.currentPos = pos;
			
			// at each step, distance-node map, distNodeMap is recaculated and sorted
			// this is done so that distances in distNodeMap are always up to date
			// and so that the drone can use any node within range
			Map<Double, Node> unsortedDistNodes = getUnsortedDistNodes();
			this.distNodeMap = sortDistNodes(unsortedDistNodes);
			
			// for every dist, node in distNodeMap...
			for (Entry<Double, Node> entry : this.distNodeMap.entrySet()) {
				double dist = entry.getKey();
				Node node = entry.getValue();
				// check if drone is in range of any nodes
				if (dist <= node.getRadius()) {
					// if so, use the node (green/red) and remove it from the list of nodes being considered
					use(node);
					this.mapNodes.removeIf(n -> n.used);
				}
				// we can break out of loop on the first iteration as the closest node will alwyas be the first entry
				// and if the first entry isn't in range then none of the others will be
				break;
			}
		}
		// next possible moves updated
		// position added to movesMadeSoFar (list of positions), used to make the path
		this.posChoices = getNextMoves();
		if (this.movesMadeSoFar.size() <= this.maxMovesAllowed) {
			this.movesMadeSoFar.add(pos);
		}
	}
}
