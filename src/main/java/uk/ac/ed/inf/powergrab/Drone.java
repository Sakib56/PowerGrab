package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

// drone super class, contains all methods and attributes shared by both stateful and stateless
public class Drone {
	static final float powerConsump = (float) -1.25;
	public float currentPower;
	public float currentCoins;
	public int maxMovesAllowed = 250;
	public Position currentPos;
	public ArrayList<Position> posChoices;
	public ArrayList<Position> movesMadeSoFar = new ArrayList<Position>();

	// Drone constructor, super class for stateful and stateless
	public Drone(Position initPos) {
		this.currentPos = initPos;
		this.currentPower = 250;
		this.currentCoins = 0;
		this.posChoices = getNextMoves(); 
		this.movesMadeSoFar.add(initPos);
	}
	
	// Makes Drone move to a specific position
	public void moveTo(Position pos) {
		// Move only occurs if Drone is still alive and the position 
		// to be moved to, pos is in the playable area
		if (isAlive() && pos.inPlayArea()) {
			// Power reduced by the power consumption, step cost
			// Current position is updated to new pos
			// Position added to movesMadeSoFar, used to make the path
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
	
	// Checks if the Drone is "alive", has enough power to make another move
	public boolean isAlive() {
		// Check against powerConsump not 0, or we'll get negative power
		return (this.currentPower > -powerConsump);
	}
	
	// prints the path to terminal
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
	}
	
	public String toString() {
		String state = currentPower <= -powerConsump ? "DEAD" : "ALIVE";
		return "<state:"+state+", coins:"+this.currentCoins+", power:"
				+this.currentPower+", #moves:"+(this.movesMadeSoFar.size()-1)+", "+this.currentPos.toString()+">";
	}
}