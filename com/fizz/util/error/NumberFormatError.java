package com.fizz.util.error;

import com.fizz.util.Position;

/**
 * Returned when the {@link Lexer} attempts to create a number Token,
 * but finds more than one decimal.
 * @author Noah James Rathman
 */
public class NumberFormatError extends Error {
	/**
	 * Creates a new NumberFormatError wit the passed details, start and end position.
	 * @param details	Details of Error
	 * @param start		Start Position of Error
	 * @param end		End Position of Error
	 */
	public NumberFormatError(String details, Position start, Position end) {
		super("Number Format", details, start, end);
	}
}