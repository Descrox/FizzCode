package com.fizz.parser;

/**
 * Used to manipulate methods in the {@link Parser} as Objects.
 * @author Noah James Rathman
 */
public interface ParseFunction {
	/**
	 * Runs the assigned {@link Parser} method for retrieval of {@link Node}s.
	 * @return ParseResult of assigned method
	 */
	ParseResult run();
}