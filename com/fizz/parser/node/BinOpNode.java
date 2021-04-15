package com.fizz.parser.node;

import com.fizz.token.Token;
import com.fizz.util.Position;

/**
 * Holds an operation between two values.
 * @author Noah James Rathman
 */
public class BinOpNode implements Node {
	private Node left, right;
	private Token op;
	
	/**
	 * Creates a new BinOpNode with the passed left node, operation, and right node.
	 * @param left		Left Node of BinOpNode
	 * @param op		Operation of BinOpNode
	 * @param right		Right Node of BinOpNode
	 */
	public BinOpNode(Node left, Token op, Node right) {
		this.left = left;
		this.op = op;
		this.right = right;
	}
	
	/**
	 * Returns the left node of this BinOpNode.
	 * @return Left Node of BinOpNode
	 */
	public Node getLeftNode() {
		return left;
	}
	
	/**
	 * Returns the operation Token of this BinOpNode.
	 * @return Operation of BinOpNode
	 */
	public Token getOperation() {
		return op;
	}
	
	/**
	 * Returns the right node of this BinOpNode.
	 * @return Right Node of BinOpNode
	 */
	public Node getRightNode() {
		return right;
	}
	
	public Position getStart() {
		return left.getStart();
	}

	public Position getEnd() {
		return right.getEnd();
	}
	
	public String toString() {
		return "(" + left + ", " + op + ", " + right + ")";
	}
}