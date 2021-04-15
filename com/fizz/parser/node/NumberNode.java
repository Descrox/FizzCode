package com.fizz.parser.node;

import com.fizz.token.Token;
import com.fizz.util.Position;

/**
 * Holds a single number value.
 * @author Noah James Rathman
 */
public class NumberNode implements Node {
	private Token value;
	
	/**
	 * Creates a new NumberNode with the passed Token value.
	 * @param value Token value
	 */
	public NumberNode(Token value) {
		this.value = value;
	}
	
	/**
	 * Returns the Token value of the NumberNode.
	 * @return Node value
	 */
	public Token getValue() {
		return value;
	}

	public Position getStart() {
		return value.getStart();
	}

	public Position getEnd() {
		return value.getEnd();
	}
	
	public String toString() {
		return value.toString();
	}
}