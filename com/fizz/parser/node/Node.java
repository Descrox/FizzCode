package com.fizz.parser.node;

import com.fizz.util.Position;

/**
 * The parent of all other Nodes. This class only holds a start and end Position,
 * but allows all other Nodes to tell where they are.
 * @author Noah James Rathman
 */
public class Node {
  private Position start, end;

  /**
   * Creates a new Node with the passed start and end.
   * The generic Node should never be instanced outside of its subclasses,
   * so its constructor is protected.
   * @param start Start of Node
   * @param end End of Node
   */
  protected Node(Position start, Position end) {
    this.start = start;
    this.end = end;
  }

  /**
   * Returns the start of this Node.
   * @return Node start Position
   */
  public Position getStart() {
    return start;
  }

  /**
   * Returns the end of this Node.
   * @return Node end Position
   */
  public Position getEnd() {
    return end;
  }
}
