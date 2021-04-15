package com.fizz.parser.node;

import java.util.ArrayList;

import com.fizz.util.Position;

public class ListNode implements Node {
	private ArrayList<Node> values;
	private Position start, end;
	
	public ListNode(ArrayList<Node> values) {
		this.values = values;
		this.start = values.get(0).getStart();
		this.end = values.get(values.size() - 1).getEnd();
	}
	
	public ListNode(Position start, Position end) {
		this.values = new ArrayList<Node>();
		this.start = start;
		this.end = end;
	}
	
	public ArrayList<Node> getValues() {
		return values;
	}

	public Position getStart() {
		return start;
	}

	public Position getEnd() {
		return end;
	}
}