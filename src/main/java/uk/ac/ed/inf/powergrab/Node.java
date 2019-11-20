package uk.ac.ed.inf.powergrab;

public class Node {
	private static final double r = 0.00025; 	// radius of effect
	public double weight;
	public double coins;
	public double power;
	public String type;
	public Position pos;
	public Position tempFromPos = null;
	
	public Node(float coins, float power, String type, Position pos) {
		this.coins = coins;
		this.power = power;
		this.type = type;
		this.pos = pos;
		this.weight = coins + power;
	}
	
	public Node(float coins, float power, String type, Position pos, Position tempFromPos) {
		this.coins = coins;
		this.power = power;
		this.type = type;
		this.pos = pos;
		this.weight = coins + power;
		this.tempFromPos = tempFromPos;
	}
	
	public String toString() {
		return "<coins:"+this.coins+", power:"+this.power+", type:"+this.type+", weight:"+
				this.weight+", "+pos.toString()+")>";
	}

	public double getRadius() {
		return r;
	}
}