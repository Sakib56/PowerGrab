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

	public void play(ArrayList<Node> mapNodes) {
		this.mapNodes = mapNodes;
		
		while (isAlive()) {
			Map<Double, Node> unsortedDistNodes = getUnsortedDistNodes();
			this.distNodeMap = sortDistNodes(unsortedDistNodes);
			Node closestGoodNode = getClosestOrdering();
			
			if (closestGoodNode == null) {
				break;
			}
			
			moveStarightTo(closestGoodNode);
			this.mapNodes.removeIf(n -> n.pos.isTheSame(closestGoodNode.pos));
		}
	}	

	public Map<Double, Node> getUnsortedDistNodes() {
		Map<Double, Node> unsortedDistNodes = new HashMap<Double, Node>();	
		for (Node node : this.mapNodes) {
			if (!node.used) {
				double fromCurrDist = this.currentPos.getL2Dist(node.pos); 		
				unsortedDistNodes.put(fromCurrDist, node);
			}
		}
		return unsortedDistNodes;
	}
	
	// Map<Double, Node> unsortedDistNodes is sorted by key (distance,
	// from a node within radius of effect and next possible move)
	public Map<Double, Node> sortDistNodes(Map<Double, Node> unsortedDistNodes) {
        Map<Double, Node> bestNextPos = new TreeMap<Double, Node>(new Comparator<Double>()
        { @Override
            public int compare(Double i, Double j) {
                return i.compareTo(j);
            }
        });
        bestNextPos.putAll(unsortedDistNodes);
        return bestNextPos;
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
