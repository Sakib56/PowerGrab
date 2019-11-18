package uk.ac.ed.inf.powergrab;

public class Position {
	static final double step = 0.0003; 	// magnitude r
	public double latitude; 			// y component
	public double longitude; 			// x component
	
	public Position(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Position nextPosition(Direction direction) {
		double newLongitude = this.longitude + step*Math.cos(direction.angle);
		double newLatitude = this.latitude + step*Math.sin(direction.angle);
		if (!inPlayArea()) { // if NOT in the playable area return old position
			return new Position(this.latitude, this.longitude);
		}
		return new Position(newLatitude, newLongitude); // otherwise return new position
	}
	
	public boolean inPlayArea() {
		// longitude in [−3.192473, −3.184319] and latitude in [55.942617, 55.946233]
		boolean withinLong = this.longitude > -3.192473 && this.longitude < -3.184319;	
		boolean withinLat = this.latitude > 55.942617 && this.latitude < 55.946233;
		return withinLong && withinLat;
	}
	
	public double getL2Dist(Position pos) { //without sqrt
		double deltaLat = Math.abs(this.latitude - pos.latitude);
		double deltaLong = Math.abs(this.longitude - pos.longitude);
		return Math.pow(deltaLat, 2) + Math.pow(deltaLong, 2);
	}
}
