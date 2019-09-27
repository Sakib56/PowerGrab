package uk.ac.ed.inf.powergrab;

public class Direction {
	public double theta;
	
	public Direction(double theta) {
		this.theta = theta;
	}
	
	// East was chosen to be 0 degrees to avoid "CAST" calculations
	// Angles in radians due to Math.cos(a) requires a to be in radians
	static final Direction E  = new Direction(0); 					// 0
	static final Direction ENE = new Direction(Math.PI * 0.125);	// 22.5
	static final Direction NE = new Direction(Math.PI * 0.25);		// 45
	static final Direction NNE = new Direction(Math.PI * 0.375);	// 67.5
	static final Direction N  = new Direction(Math.PI * 0.5);		// 90
	static final Direction NNW = new Direction(Math.PI * 0.625);	// 112.5
	static final Direction NW = new Direction(Math.PI * 0.75);		// 135
	static final Direction WNW = new Direction(Math.PI * 0.875);	// 157.5
	static final Direction W  = new Direction(Math.PI);				// 180
	static final Direction WSW = new Direction(Math.PI * 1.125);	// 202.5
	static final Direction SW = new Direction(Math.PI * 1.25);		// 225
	static final Direction SSW = new Direction(Math.PI * 1.375);	// 247.5
	static final Direction S  = new Direction(Math.PI * 1.5);		// 270
	static final Direction SSE = new Direction(Math.PI * 1.625);	// 292.5
	static final Direction SE = new Direction(Math.PI * 1.75);		// 315
	static final Direction ESE = new Direction(Math.PI * 1.875);	// 337.5
}