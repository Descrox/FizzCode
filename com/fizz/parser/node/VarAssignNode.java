package com.fizz.parser.node;

import com.fizz.token.Token;
import com.fizz.util.Position;

public class VarAssignNode implements Node {
	private Token name;
	private Node value;
	
	public VarAssignNode(Token name, Node value) {
		this.name = name;
		this.value = value;
	}
	
	public Token getName() {
		return name;
	}
	
	public Node getValue() {
		return value;
	}
	
	public Position getStart() {
		return name.getStart();
	}

	public Position getEnd() {
		return value.getEnd();
	}
}