package uk.ac.ed.inf.powergrab;

public class Node {
	public double coins;
	public double power;
	public String type;
	
	public Node(float coins, float power, String type) {
		this.coins = coins;
		this.power = power;
		this.type = type;
	}
	
	public String toString() {
		return "<coins:" + this.coins + ", power:" + this.power + ", type:" +this.type + ">";
	}
}
