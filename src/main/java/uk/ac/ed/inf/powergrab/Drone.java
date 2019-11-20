package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

public class Drone {
	static final float powerConsump = (float) -1.25;
	public float currentPower;
	public float currentCoins;
	public Position currentPos;
	public ArrayList<Position> posChoices;

	public Drone(Position pos) {
		this.currentPos = pos;
		this.currentPower = 250;
		this.currentCoins = 0;
		this.posChoices = getNextMoves(); 
	}
	
	public void moveTo(Position pos, float extraPower, float extraCoins) {
		System.out.println(toString());
		this.currentPos = pos;
		this.currentPower += (extraPower + powerConsump);
		this.currentCoins += extraCoins;
		this.posChoices = getNextMoves();
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
		return "<state:"+state+", coins:"+this.currentCoins+", power: "+this.currentPower+
				", pos("+this.currentPos.latitude +", "+ this.currentPos.longitude+")>";
	}
}