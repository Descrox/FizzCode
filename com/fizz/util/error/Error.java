package com.fizz.util.error;

import com.fizz.util.Position;

/**
 * Used for the display of issues found while compiling
 * and running Fizz code.
 * @author Noah James Rathman
 */
public class Error {
  /**
   * Name of Error.
   */
  protected String name;
  /**
   * Description of Error.
   */
  protected String details;
  /**
   * Start of Error
   */
  protected Position start;
  /**
   * End of Error
   */
  protected Position end;

  /**
   * Creates a new Error with the passed name, details, start and end.
   * The generic Error should never be used, so this constructor is protected.
   * @param name Name of Error
   * @param details Description of Error
   * @param start Start of Error
   * @param end End of Error
   */
  protected Error(String name, String details, Position start, Position end) {
    this.name = name;
    this.details = details;
    this.start = start;
    this.end = end;
  }

  private String getLine() {
    final String FULL = start.getFileText();
    int sIdx = start.getIndex();
    int eIdx = start.getIndex();

    while(sIdx > 0 && FULL.charAt(sIdx - 1) != '\n') {
      sIdx--;
    }

    while(eIdx < FULL.length() && FULL.charAt(eIdx) != '\n') {
      eIdx++;
    }

    return FULL.substring(sIdx, eIdx);
  }

  /**
   * Creates a display of where the Error is located in the file.
   * @return Display of Error
   */
  protected String createDisplay() {
    final String LINE = getLine();
    String arrows = "";
    int sIdx = start.getColumn() - 1, eIdx = end.getColumn() - 1;

    for(int i = 0; i < eIdx; i++) {
      char c = (i >= sIdx ? '^' : ' ');
      arrows += c;
    }

    return LINE + "\n|  " + arrows;
  }

  public String toString() {
    String error = "|  Error: " + name;
    error += "\n|    " + start;
    error += "\n|  " + createDisplay();
    error += "\n|  " + details;

    return error;
  }
}
