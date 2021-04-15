package com.fizz.util.error;

import com.fizz.util.Position;

/**
 * Returned when the {@link Parser} finds invalid syntax.
 * @author Noah James Rathman
 */
public class InvalidSyntaxError extends Error {
	/**
	 * Creates an InvalidSyntaxError with the passed details, start and end position.
	 * @param details	Details of Error
	 * @param start		Start Position of Error
	 * @param end		End Position of Error
	 */
	public InvalidSyntaxError(String details, Position start, Position end) {
		super("Invalid Syntax", details, start, end);
	}
}