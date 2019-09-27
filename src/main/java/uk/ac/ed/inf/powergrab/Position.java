package uk.ac.ed.inf.powergrab;

public class Position {
	static final double step = 0.0003; // magnitude r
	public double longitude; // x component
	public double latitude; // y component
	
	public Position(double latitude, double longitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public Position nextPosition(Direction direction) {
		double newLongitude = this.longitude + step*Math.cos(direction.theta);
		double newLatitude = this.latitude + step*Math.sin(direction.theta);
		return new Position(newLongitude, newLatitude);
	}
	
	public boolean inPlayArea() {
		// longitude in [−3.192473, −3.184319] and latitude in [55.942617, 55.946233]
		boolean withinLong = this.longitude >= -3.192473 && this.longitude <= -3.184319;	
		boolean withinLat = this.longitude >= 55.942617 && this.longitude <= 55.946233;
		return withinLong && withinLat;
	}
}
