package com.fizz.parser;

import com.fizz.parser.node.Node;
import com.fizz.token.Token;
import com.fizz.util.error.Error;

/**
 * Used for authentication of results in the Parser.
 * @author Noah James Rathman
 */
public class ParseResult {
	private Node result;
	private Error error;
	
	/**
	 * Creates a new ParseResult, setting the result and error values
	 * to null.
	 */
	public ParseResult() {
		result = null;
		error = null;
	}
	
	/**
	 * Returns the result of this ParseResult.
	 * @return ParseResult result
	 */
	public Node getResult() {
		return result;
	}
	
	/**
	 * Returns the error of this ParseResult.
	 * @return ParseResult error
	 */
	public Error getError() {
		return error;
	}
	
	/**
	 * Registers the passed value. If the passed value is an instance of
	 * ParseResult, this Error is assigned to its Error and its value is
	 * returned. If the passed value isn't an instance of ParseResult,
	 * it is casted to a {@link Node} and returned.
	 * @param value Value to be registered
	 * @return Registered value
	 */
	public Node register(Object value) {
		if(value instanceof ParseResult) {
			error = ((ParseResult) value).getError();
			return ((ParseResult) value).getResult();
		} else if(value instanceof Token) {
			return null;
		}
		
		return (Node) value;
	}
	
	/**
	 * Assigns this result Node to the passed <b>result</b> and returns this ParseResult.
	 * @param result Successful Node
	 * @return Successful ParseResult
	 */
	public ParseResult success(Node result) {
		this.result = result;
		return this;
	}
	
	/**
	 * Assigns this Error to the passed <b>error</b> and returns this ParseResult.
	 * @param error Fail Error
	 * @return Failed ParseResult
	 */
	public ParseResult failure(Error error) {
		this.error = error;
		return this;
	}
}