package com.fizz.lexer;

import java.util.ArrayList;

import com.fizz.token.Token;
import com.fizz.token.TokenType;
import com.fizz.util.Constants;
import com.fizz.util.Position;
import com.fizz.util.error.Error;
import com.fizz.util.error.ExpectedCharError;
import com.fizz.util.error.IllegalCharError;
import com.fizz.util.error.NumberFormatError;

/**
 * Manages all lexical analysis of the passed input text.
 * @author Noah James Rathman
 */
public class Lexer {
	private String fText;
	private Position pos;
	private char curChar;
	
	/**
	 * Creates a new Lexer instance with the passed file name and text.
	 * @param fName <i>Name of File</i>
	 * @param fText <i>Content of File</i>
	 */
	public Lexer(String fName, String fText) {
		this.fText = fText;
		pos = new Position(-1, 1, 0, fName, fText);
		curChar = 0;
		
		advance();
	}
	
	/**
	 * Advances the Lexer to the next character.
	 */
	private void advance() {
		pos.advance(curChar);
		
		if(pos.getIndex() < fText.length()) {
			curChar = fText.charAt(pos.getIndex());
		} else {
			curChar = 0;
		}
	}
	
	/**
	 * Splits the file content into an array of {@link Token}s.
	 * @return Any Errors found while creating tokens. If no
	 * Errors are found, an ArrayList of Tokens are returned.
	 */
	public Object createTokenArray() {
		ArrayList<Token> tokens = new ArrayList<Token>();
		Position start = null;
		
		while(curChar != 0) {
			start = pos.copy();
			
			if(curChar == ' ' || curChar == '\t') {
				advance();
			} else if(Character.isAlphabetic(curChar)) {
				tokens.add(makeIdentifier());
			} else if(Character.isDigit(curChar)) {
				Object numRes = makeNumber();
				
				if(numRes instanceof Error) {
					return numRes;
				}
				
				tokens.add((Token) numRes);
			} else if(curChar == '+') {
				advance();
				tokens.add(new Token(TokenType.ADD, start, pos.copy()));
			} else if(curChar == '-') {
				advance();
				tokens.add(new Token(TokenType.SUB, start, pos.copy()));
			} else if(curChar == '*') {
				advance();
				tokens.add(new Token(TokenType.MUL, start, pos.copy()));
			} else if(curChar == '/') {
				advance();
				tokens.add(new Token(TokenType.DIV, start, pos.copy()));
			} else if(curChar == '(') {
				advance();
				tokens.add(new Token(TokenType.LPAREN, start, pos.copy()));
			} else if(curChar == ')') {
				advance();
				tokens.add(new Token(TokenType.RPAREN, start, pos.copy()));
			} else if(curChar == ':') {
				advance();
				tokens.add(new Token(TokenType.COLON, start, pos.copy()));
			} else if(curChar == ',') {
				advance();
				tokens.add(new Token(TokenType.COMMA, start, pos.copy()));
			} else if(curChar == '[') {
				advance();
				tokens.add(new Token(TokenType.LSQUARE, start, pos.copy()));
			} else if(curChar == ']') {
				advance();
				tokens.add(new Token(TokenType.RSQUARE, start, pos.copy()));
			} else if(curChar == '"') {
				Object strRes = makeString();
				
				if(strRes instanceof Error) {
					return strRes;
				}
				
				tokens.add((Token) strRes);
			} else if(curChar == '=') {
				tokens.add(makeEquals());
			} else if(curChar == '!') {
				tokens.add(makeNot());
			} else if(curChar == '<') {
				tokens.add(makeLessThan());
			} else if(curChar == '>') {
				tokens.add(makeGreaterThan());
			} else {
				char cur = curChar;
				advance();
				return new IllegalCharError("Character \"" + cur + "\" isn't recognized.", start, pos.copy());
			}
		}
		
		start = pos.copy();
		advance();
		tokens.add(new Token(TokenType.EOF, start, pos.copy()));
		
		return tokens;
	}
	
	/**
	 * Creates a number Token.
	 * @return Any Errors found while creating the number.
	 * If no Errors are found, a number token is returned.
	 */
	private Object makeNumber() {
		Position start = pos.copy();
		String numRepr = "";
		int dec = 0;
		
		while(curChar != 0 && (Character.isDigit(curChar) || curChar == '.')) {
			if(curChar == '.') {
				if(dec > 0) {
					advance();
					return new NumberFormatError("Numbers cannot contain multiple decimals.", start, pos.copy());
				}
				
				dec++;
			}
			
			numRepr += curChar;
			advance();
		}
		
		if(dec == 0) {
			return new Token(TokenType.INT, Long.valueOf(numRepr), start, pos.copy());
		} else {
			return new Token(TokenType.FLOAT, Double.valueOf(numRepr), start, pos.copy());
		}
	}
	
	private Object makeString() {
		Position start = pos.copy();
		String str = "";
		
		advance();
		
		while(curChar != 0 && curChar != '"') {
			str += curChar;
			advance();
		}
		
		if(curChar != '"') {
			start = pos.copy();
			
			advance();
			
			return new ExpectedCharError("Expected '\"'.", start, pos.copy());
		}
		
		advance();
		
		return new Token(TokenType.STR, str, start, pos.copy());
	}
	
	private Token makeIdentifier() {
		Position start = pos.copy();
		String idRepr = "";
		
		while(curChar != 0 && (Character.isAlphabetic(curChar) || curChar == '_')) {
			idRepr += curChar;
			advance();
		}
		
		if(Constants.isKeyword(idRepr)) {
			return new Token(TokenType.KEY, idRepr, start, pos.copy());
		} else {
			return new Token(TokenType.ID, idRepr, start, pos.copy());
		}
	}
	
	private Token makeEquals() {
		Position start = pos.copy();
		advance();
		
		if(curChar == '=') {
			advance();
			return new Token(TokenType.EE, start, pos.copy());
		}
		
		return new Token(TokenType.EQ, start, pos.copy());
	}
	
	private Token makeNot() {
		Position start = pos.copy();
		advance();
		
		if(curChar == '=') {
			advance();
			return new Token(TokenType.NE, start, pos.copy());
		}
		
		return new Token(TokenType.NOT, start, pos.copy());
	}
	
	private Token makeLessThan() {
		Position start = pos.copy();
		advance();
		
		if(curChar == '=') {
			advance();
			return new Token(TokenType.LTE, start, pos.copy());
		}
		
		return new Token(TokenType.LT, start, pos.copy());
	}
	
	private Token makeGreaterThan() {
		Position start = pos.copy();
		advance();
		
		if(curChar == '=') {
			advance();
			return new Token(TokenType.GTE, start, pos.copy());
		}
		
		return new Token(TokenType.GT, start, pos.copy());
	}
}