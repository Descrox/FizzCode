package com.fizz.token;

import com.fizz.util.Position;

/**
 * Represents a recognized snippet of text.
 * @author Noah James Rathman
 */
public class Token {
	private TokenType type;
	private Object value;
	private Position start, end;
	
	/**
	 * Creates a new Token with the passed type, value, start and end position.
	 * @param type		Type of Token
	 * @param value		Value of Token
	 * @param start		Start Position of Token
	 * @param end		End Position of Token
	 */
	public Token(TokenType type, Object value, Position start, Position end) {
		this.type = type;
		this.value = value;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Creates a new Token with the passed type, start and end position.
	 * @param type		Type of Token
	 * @param start		Start Position of Token
	 * @param end		End Position of Token
	 */
	public Token(TokenType type, Position start, Position end) {
		this.type = type;
		this.value = null;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Returns the type of this Token.
	 * @return TokenType of Token
	 */
	public TokenType getType() {
		return type;
	}
	
	/**
	 * Returns the value of this Token.
	 * @return Value of Token
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns the start Position of this Token.
	 * @return Start Position of Token
	 */
	public Position getStart() {
		return start;
	}
	
	/**
	 * Returns the end Position of this Token.
	 * @return End Position of Token
	 */
	public Position getEnd() {
		return end;
	}
	
	/**
	 * Tests if this Token's type matches any of this
	 * passed TokenTypes.
	 * @param types Array of TokenTypes
	 * @return True if this Token's type matches any of
	 * the passed types, else false.
	 */
	public boolean isType(TokenType...types) {
		for(TokenType t : types) {
			if(type.equals(t)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Tests if this Token matches the passed type and values.
	 * @param type		Type of Token
	 * @param value		Values of Token
	 * @return True if this Token matches the passed parameters,
	 * and false otherwise.
	 */
	public boolean matches(TokenType type, Object[] values) {
		for(Object obj : values) {
			if(this.type.equals(type) && value.equals(obj)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String toString() {
		return type + (value == null ? "":":" + value);
	}
}