package uk.ac.ed.inf.powergrab;

public class Node {
	public double coins;
	public double power;
	public String type;
	public Position pos;
	
	public Node(float coins, float power, String type, Position pos) {
		this.coins = coins;
		this.power = power;
		this.type = type;
		this.pos = pos;
	}
	
	public String toString() {
		return "<coins:" + this.coins + ", power:" + this.power + ", type:" + this.type
				+ ", pos(" + this.pos.latitude + ", " + this.pos.longitude + ")>";
	}
}
