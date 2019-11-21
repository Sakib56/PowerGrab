package uk.ac.ed.inf.powergrab;

public class Node {
	private static final double r = 0.00025; 	// radius of effect
	public boolean used = false;
	public Position tempFromPos = null;	// temp buffer to store a pos that drone can get to s.t. in radius of this node
	public double weight = 0;
	public double coins = 0;
	public double power = 0;
	public String type;
	public Position pos;
	
	public Node(float coins, float power, String type, Position pos) {
		this.coins = coins;
		this.power = power;
		this.type = type;
		this.pos = pos;
		this.weight = getWeight();
	}
	
	public Node(float coins, float power, String type, Position pos, Position tempFromPos) {
		this.coins = coins;
		this.power = power;
		this.type = type;
		this.pos = pos;
		this.weight = getWeight();
		this.tempFromPos = tempFromPos;
	}
	
	public String toString() {
		return "<coins:"+this.coins+", power:"+this.power+", type:"+this.type+", weight:"+
				this.weight+", used:"+this.used+", "+pos.toString()+")>";
	}

	public double getRadius() {
		return r;
	}

	public double getWeight() {
		return this.coins + this.power;
	}
}