package com.fizz.parser.node;

import com.fizz.lexer.token.Token;

/**
 * Holds a Token which has a direct value <i>(int, float or string)</i>.
 * @author Noah James Rathman
 */
public final class ValueNode extends Node {
  private Token value;

  /**
   * Creates a new ValueNode with the passed Token value.
   * @param value Token value
   */
  public ValueNode(Token value) {
    super(value.getStart(), value.getEnd());
    this.value = value;
  }

  /**
   * Returns the value of this Node.
   * @return Node value
   */
  public Token getValue() {
    return value;
  }

  public String toString() {
    return value.toString();
  }
}
