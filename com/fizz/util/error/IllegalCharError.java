package com.fizz.util.error;

import com.fizz.util.Position;

/**
 * Returned when the {@link Lexer} finds a character it doesn't recognize.
 * @author Noah James Rathman
 */
public class IllegalCharError extends Error {
	/**
	 * Creates an IllegalCharError with the passed details, start and end position.
	 * @param details	Details of Error
	 * @param start		Start Position of Error
	 * @param end		End Position of Error
	 */
	public IllegalCharError(String details, Position start, Position end) {
		super("Illegal Character", details, start, end);
	}
}