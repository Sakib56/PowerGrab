package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

public class Drone {
	public Position currentPos;
	public ArrayList<Position> posChoices;

	public Drone(Position pos) {
		this.currentPos = pos;
		this.posChoices = getNextMoves(); 
	}
	
	public void moveTo(Position pos) {
		this.currentPos = pos;
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
}