package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

public class Drone {
	static final float powerConsump = (float) -1.25;
	public float currentPower;
	public float currentCoins;
	public Position currentPos;
	public ArrayList<Position> posChoices;

	public Drone(Position initPos) {
		this.currentPos = initPos;
		this.currentPower = 250;
		this.currentCoins = 0;
		this.posChoices = getNextMoves(); 
	}
	
	public void moveTo(Position pos) {
		System.out.println(toString());
		this.currentPos = pos;
		this.posChoices = getNextMoves();
	}
	
	public void use(Node node) {
		this.currentPower += (node.power + powerConsump);
		this.currentCoins += node.coins;
		
		node.coins = 0;
		node.power = 0;
		node.weight = node.getWeight();
		node.used = true;
	}
	
	public ArrayList<Position> getNextMoves() {
		List<Direction> allDirs = new Direction().getAllDirs();
		ArrayList<Position> posChoices = new ArrayList<Position>();
		
		for (Direction dir : allDirs) {
			posChoices.add(this.currentPos.nextPosition(dir));
		}
		return posChoices;
	}
	
	public String toString() {
		String state = currentPower <= 0 ? "DEAD" : "ALIVE";
		return "<state:"+state+", coins:"+this.currentCoins+", power: "
				+this.currentPower+", "+this.currentPos.toString()+">";
	}
}