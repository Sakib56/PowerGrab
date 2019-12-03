package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

public class Drone {
	static final float powerConsump = (float) -1.25;
	public float currentPower;
	public float currentCoins;
	public int maxMovesAllowed = 250;
	public Position currentPos;
	public ArrayList<Position> posChoices;
	public ArrayList<Position> movesMadeSoFar = new ArrayList<Position>();

	public Drone(Position initPos) {
		this.currentPos = initPos;
		this.currentPower = 250;
		this.currentCoins = 0;
		this.posChoices = getNextMoves(); 
		this.movesMadeSoFar.add(initPos);
	}
	
	public void moveTo(Position pos) {
		if (isAlive() && pos.inPlayArea()) {
			this.currentPower += powerConsump;
			this.currentPos = pos;
			this.movesMadeSoFar.add(pos);
		}
		this.posChoices = getNextMoves();
	}
	
	public void use(Node node) {
		if (isAlive() && this.currentPos.inPlayArea()) {
			this.currentPower += node.power;
			this.currentCoins += node.coins;
			
			node.coins = 0;
			node.power = 0;
			node.weight = node.getWeight();
			node.used = true;
		}
	}
	
	public ArrayList<Position> getNextMoves() {
		List<Direction> allDirs = new Direction().getAllDirs();
		ArrayList<Position> posChoices = new ArrayList<Position>();
		
		for (Direction dir : allDirs) {
			Position nextPos = this.currentPos.nextPosition(dir);
			if (nextPos.inPlayArea()) {
				posChoices.add(nextPos);
			}
		}
		return posChoices;
	}
	
	public boolean isAlive() {
		return (this.currentPower > -powerConsump);
	}
	
	public void printPath() {
    	String coordinates = "";
    	for (int i=0; i<this.movesMadeSoFar.size(); i++) {
    		Position pos = this.movesMadeSoFar.get(i);
    		String coord = "          ["+pos.longitude+", "+pos.latitude+"]";
			
			if (i == this.movesMadeSoFar.size()-1) {
				coordinates += coord+"\r\n";
			} else {
				coordinates += coord+",\r\n";
			}
    	}
    	
    	System.out.println("    {\r\n" + 
    			"      \"type\": \"Feature\",\r\n" + 
    			"      \"properties\": { },\r\n" + 
    			"      \"geometry\": {\r\n" + 
    			"        \"type\": \"LineString\",\r\n" + 
    			"        \"coordinates\": [\r\n" + 
    			coordinates +
    			"        ]\r\n" + 
    			"      }\r\n" + 
    			"    },");	

//    	System.out.println("\n"+d.toString());
	}
	
	public String toString() {
		String state = currentPower <= -powerConsump ? "DEAD" : "ALIVE";
		return "<state:"+state+", coins:"+this.currentCoins+", power:"
				+this.currentPower+", #moves:"+(this.movesMadeSoFar.size()-1)+", "+this.currentPos.toString()+">";
	}
}