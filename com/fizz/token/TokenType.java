package com.fizz.token;

/**
 * Type values used for Token identification.
 * @author Noah James Rathman
 */
public enum TokenType {
	/**
	 * Integer value identifier.
	 */
	INT,
	/**
	 * Float value identifier.
	 */
	FLOAT,
	/**
	 * String value identifier.
	 */
	STR,
	/**
	 * Keyword value identifier.
	 */
	KEY,
	/**
	 * Identifier value identifier.
	 */
	ID,
	
	/**
	 * Addition operation identifier.
	 */
	ADD,
	/**
	 * Subtraction operation identifier.
	 */
	SUB,
	/**
	 * Multiplication operation identifier.
	 */
	MUL,
	/**
	 * Division operation identifier.
	 */
	DIV,
	/**
	 * Equals operation identifier.
	 */
	EQ,
	/**
	 * Equal To operation identifier.
	 */
	EE,
	/**
	 * Not Equal To operation identifier.
	 */
	NE,
	/**
	 * Less Than operation identifier.
	 */
	LT,
	/**
	 * Greater Than operation identifier.
	 */
	GT,
	/**
	 * Less Than or Equal To operation identifier.
	 */
	LTE,
	/**
	 * Greater Than or Equal To operation identifier.
	 */
	GTE,
	/**
	 * Not operation identifier.
	 */
	NOT,
	
	/**
	 * Left Parenthesis symbol identifier.
	 */
	LPAREN,
	/**
	 * Right Parenthesis symbol identifier.
	 */
	RPAREN,
	/**
	 * Colon symbol identifier.
	 */
	COLON,
	/**
	 * Comma symbol identifier.
	 */
	COMMA,
	/**
	 * Left Square Bracket symbol identifier.
	 */
	LSQUARE,
	/**
	 * Right Square Bracket symbol identifier.
	 */
	RSQUARE,
	
	/**
	 * End of File identifier.
	 */
	EOF
}