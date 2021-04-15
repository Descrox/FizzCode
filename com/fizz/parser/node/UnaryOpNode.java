package com.fizz.parser.node;

import com.fizz.token.Token;
import com.fizz.util.Position;

/**
 * Holds a single value operation.
 * @author Noah James Rathman
 */
public class UnaryOpNode implements Node {
	private Token op;
	private Node value;
	
	/**
	 * Creates a new UnaryOpNode with the passed operation and value.
	 * @param op		Operation of UnaryOpNode
	 * @param value		Value of UnaryOpNode
	 */
	public UnaryOpNode(Token op, Node value) {
		this.op = op;
		this.value = value;
	}
	
	/**
	 * Returns the Token operation of this UnaryOpNode.
	 * @return Operation of UnaryOpNode
	 */
	public Token getOperation() {
		return op;
	}
	
	/**
	 * Returns the value of this UnaryOpNode.
	 * @return Value of UnaryOpNode
	 */
	public Node getValue() {
		return value;
	}

	public Position getStart() {
		return op.getStart();
	}

	public Position getEnd() {
		return value.getEnd();
	}
	
	public String toString() {
		return "(" + op + ", " + value + ")";
	}
}