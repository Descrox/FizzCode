package com.fizz.parser.node;

import com.fizz.util.Position;

/**
 * Represents a command to control the flow of loops.
 * @author Noah James Rathman
 */
public class ControlNode extends Node {
  private String type;

  /**
   * Creates a new ControlNode with the passed type, start and end.
   * @param type Type of Node
   * @param start Start of Node
   * @param end End of Node
   */
  public ControlNode(String type, Position start, Position end) {
    super(start, end);

    this.type = type;
  }

  /**
   * Returns this Node's control type.
   * @return Type of Node
   */
  public String getType() {
    return type;
  }
}
