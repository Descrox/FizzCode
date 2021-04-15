package com.fizz.util;

/**
 * Used to the track the position of Tokens, Nodes and Values.
 * @author Noah James Rathman
 */
public final class Position {
	private String fName, fText;
	private int index, line, column;
	
	/**
	 * Creates a new Position with the passed index, line, column, file name and file text.
	 * @param index		Position text index
	 * @param line		Position text line
	 * @param column	Position text column
	 * @param fName		Position file name
	 * @param fText		Position file text
	 */
	public Position(int index, int line, int column, String fName, String fText) {
		this.index = index;
		this.line = line;
		this.column = column;
		this.fName = fName;
		this.fText = fText;
	}
	
	/**
	 * Advances this Position to the next index.
	 * Line and column values are updated as well.
	 * @param curChar Current Lexer character
	 */
	public void advance(char curChar) {
		index++;
		column++;
		
		if(curChar == '\n') {
			column = 1;
			line++;
		}
	}
	
	/**
	 * Returns a copy of this Position.
	 * @return Position copy
	 */
	public Position copy() {
		return new Position(index, line, column, fName, fText);
	}
	
	/**
	 * Returns the index of this Position.
	 * @return Position index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Returns the line of this Position.
	 * @return Position line
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * Returns the column of this Position.
	 * @return Position column
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Returns the file name of this Position.
	 * @return Position file name
	 */
	public String getFileName() {
		return fName;
	}
	
	/**
	 * Returns the file text of this Position.
	 * @return Position file text
	 */
	public String getFileText() {
		return fText;
	}
	
	public String toString() {
		return "File "+ fName + ": line " + line + ", column " + column;
	}
}