package com.fizz.util;

/**
 * Used to identify positions in Fizz files.
 * @author Noah James Rathman
 */
public final class Position {
  private String fName, fText;
  private int idx, ln, col;

  /**
   * Creates a Position at the start of a file.
   * @param fName File name
   * @param fText File text
   */
  public Position(String fName, String fText) {
    this.fName = fName;
    this.fText = fText;
    idx = -1;
    ln = 1;
    col = 0;
  }

  private Position(String fName, String fText, int idx, int ln, int col) {
    this.fName = fName;
    this.fText = fText;
    this.idx = idx;
    this.ln = ln;
    this.col = col;
  }

  /**
   * Advances the Position to the next character.
   * @param curChar Current character
   */
  public void advance(char curChar) {
    idx++;
    col++;

    if(curChar == '\n') {
      col = 1;
      ln++;
    }
  }

  /**
   * Returns a copy of this Position.
   * @return Position copy
   */
  public Position copy() {
    return new Position(fName, fText, idx, ln, col);
  }

  /**
   * Returns the file text of this Position.
   * @return Position file text
   */
  public String getFileText() {
    return fText;
  }

  /**
   * Returns the index of this Position.
   * @return Position index
   */
  public int getIndex() {
    return idx;
  }

  /**
   * Returns the column of this Position.
   * @return Position column
   */
  public int getColumn() {
    return col;
  }

  public String toString() {
    return "File " + fName + " at " + ln + ":" + col;
  }
}
