package uk.ac.ed.inf.powergrab;

import java.util.Arrays;
import java.util.List;

public class Direction {
	public double angle;
	
	public Direction() {}
	
	public Direction(double angle) {
		this.angle = angle;
	}
	
	public List<Direction> getAllDirs() {
		List<Direction> allDirs = Arrays.asList(E, ENE, NE, NNE, N, NNW, NW, WNW, W, WSW, SW, SSW, S, SSE, SE, ESE);
		return allDirs;
	}
	
	// East was chosen to be 0 degrees to avoid "CAST" calculations
	// Angles in radians due to Math.cos(a) requires a to be in radians
	static final Direction E  = new Direction(0); 					// 0
	static final Direction ENE = new Direction(0.125 * Math.PI);	// 22.5
	static final Direction NE = new Direction(0.25 * Math.PI);		// 45
	static final Direction NNE = new Direction(0.375 * Math.PI);	// 67.5
	static final Direction N  = new Direction(0.5 * Math.PI);		// 90
	static final Direction NNW = new Direction(0.625 * Math.PI);	// 112.5
	static final Direction NW = new Direction(0.75 * Math.PI);		// 135
	static final Direction WNW = new Direction(0.875 * Math.PI);	// 157.5
	static final Direction W  = new Direction(Math.PI);				// 180
	static final Direction WSW = new Direction(1.125 * Math.PI);	// 202.5
	static final Direction SW = new Direction(1.25 * Math.PI);		// 225
	static final Direction SSW = new Direction(1.375 * Math.PI);	// 247.5
	static final Direction S  = new Direction(1.5 * Math.PI);		// 270
	static final Direction SSE = new Direction(1.625 * Math.PI);	// 292.5
	static final Direction SE = new Direction(1.75 * Math.PI);		// 315
	static final Direction ESE = new Direction(1.875 * Math.PI);	// 337.5
}