package uk.ac.ed.inf.powergrab;

public class Move {
	public Position pos;
	public Node nodeCloseBy = null;

	public Move(Position pos, Node nodeCloseBy) {
		this.pos = pos;
		this.nodeCloseBy = nodeCloseBy;
	}
	
	public Move(Position pos) {
		this.pos = pos;
	}
	
	public boolean hasNodeCloseBy() {
		return this.nodeCloseBy != null;
	}
}
