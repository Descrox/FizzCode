package com.fizz.parser.node;

import java.util.List;

import com.fizz.util.Position;

/**
 * Represents a list of values.
 * @author Noah James Rathman
 */
public class ArrayNode extends Node {
  private List<Node> values;

  /**
   * Creates a new ArrayNode with no values.
   * @param start Start of Node
   * @param end End of Node
   */
  public ArrayNode(Position start, Position end) {
    super(start, end);
    values = null;
  }

  /**
   * Creates a new ArrayNode with the passed values.
   * @param values Array values
   */
  public ArrayNode(List<Node> values) {
    super(values.get(0).getStart(), values.get(values.size() - 1).getEnd());
    this.values = values;
  }

  /**
   * Returns this ArrayNode's List of Node values.
   * @return Array values
   */
  public List<Node> getValues() {
    return values;
  }
}
