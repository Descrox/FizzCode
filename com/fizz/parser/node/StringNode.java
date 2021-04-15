package com.fizz.parser.node;

import com.fizz.token.Token;
import com.fizz.util.Position;

public class StringNode implements Node {
	private Token value;
	
	public StringNode(Token value) {
		this.value = value;
	}
	
	public Token getValue() {
		return value;
	}
	
	public Position getStart() {
		return value.getStart();
	}

	public Position getEnd() {
		return value.getEnd();
	}

}