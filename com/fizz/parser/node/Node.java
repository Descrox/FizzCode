package com.fizz.parser.node;

import com.fizz.util.Position;

/**
 * Used for determining all Node positions.
 * @author Noah James Rathman
 */
public interface Node {
	/**
	 * Returns the start Position of the Node.
	 * @return Node Start Position
	 */
	Position getStart();
	/**
	 * Returns the end Position of the Node.
	 * @return Node End Position
	 */
	Position getEnd();
}