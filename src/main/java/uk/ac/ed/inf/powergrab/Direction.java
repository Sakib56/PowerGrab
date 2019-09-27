package uk.ac.ed.inf.powergrab;

public class Direction {
	public double angle;
	
	public Direction(double angle) {
		this.angle = angle;
	}
	
	// East was chosen to be 0 degrees to avoid "CAST" calculations
	static final Direction E = new Direction(0);
	static final Direction ENE = new Direction(22.5);
	static final Direction NE = new Direction(45);
	static final Direction NNE = new Direction(67.5);
	static final Direction N = new Direction(90);
	static final Direction NNW = new Direction(112.5);
	static final Direction NW = new Direction(135);
	static final Direction WNW = new Direction(157.5);
	static final Direction W = new Direction(180);
	static final Direction WSW = new Direction(202.5);
	static final Direction SW = new Direction(225);
	static final Direction SSW = new Direction(247.5);
	static final Direction S = new Direction(270);
	static final Direction SSE = new Direction(292.5);
	static final Direction SE = new Direction(315);
	static final Direction ESE = new Direction(337.5);
}
