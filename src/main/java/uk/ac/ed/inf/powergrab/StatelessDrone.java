package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

public class StatelessDrone extends Drone {
	private Move bestPos;
	private Random randomSeed;

	public StatelessDrone(Position initPos, long randomSeed) {
		super(initPos);
		this.randomSeed = new Random(randomSeed); 
	}

	public void play(ArrayList<Node> mapNodes) {
		for (int i=0; i<this.maxMovesAllowed; i++) {
			if (isAlive()) {
				Map<Double, Node> nextDistPosMap = getUnsortedBestNextPos(mapNodes);
				nextDistPosMap = sortBestNextPos(nextDistPosMap);
				
				Move bestNextMove = pickBestPos(nextDistPosMap);
				moveTo(bestNextMove.pos);
				
				if (bestNextMove.hasNodeCloseBy()) {
					use(bestNextMove.nodeCloseBy);
					mapNodes.removeIf(n -> n.pos == bestNextMove.nodeCloseBy.pos);
				}
			} else {
				break;
			}
		}
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
	
	// Map<Double, Node> unsortedBestNextPos is sorted by key (distance,
	// from a node within radius of effect and next possible move)
	public Map<Double, Node> sortBestNextPos(Map<Double, Node> unsortedBestNextPos) {
        Map<Double, Node> bestNextPos = new TreeMap<Double, Node>(new Comparator<Double>()
        { @Override
            public int compare(Double i, Double j) {
                return i.compareTo(j);
            }
        });
        bestNextPos.putAll(unsortedBestNextPos);
        return bestNextPos;
	}
	
	public Move pickBestPos(Map<Double, Node> nextDistPosMap) {
		ArrayList<Position> possibleNextPos = getNextMoves();
		bestPos = new Move(getRandom(possibleNextPos));
		double maxWeight = -999;
		
		for (Entry<Double, Node> entry : nextDistPosMap.entrySet()) {
			Node node = entry.getValue();
			
			if (node.weight > maxWeight) {
				maxWeight = node.weight;
				possibleNextPos.removeIf(p -> p.isTheSame(node.tempFromPos));
				bestPos = new Move(node.tempFromPos, node);
			}
		}
		
		if (maxWeight < 0 && !possibleNextPos.isEmpty()) {
			bestPos = new Move(getRandom(possibleNextPos));
		}
		
		return bestPos;
	}
	
	// Gets a random element out of an ArrayList<Position>, possibleNextPos
	public Position getRandom(ArrayList<Position> possibleNextPos) {
	    int rnd = randomSeed.nextInt(possibleNextPos.size());
	    return possibleNextPos.get(rnd);
	}
}
