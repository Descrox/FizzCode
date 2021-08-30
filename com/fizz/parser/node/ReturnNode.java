package com.fizz.parser.node;

/**
 * Allows a StatementNode to return a non-void value.
 * @author Noah James Rathman
 */
public class ReturnNode extends Node {
  private Node expr;

  /**
   * Creates a new ReturnNode with the passed expression.
   * @param expr Return expression
   */
  public ReturnNode(Node expr) {
    super(expr.getStart(), expr.getEnd());
    this.expr = expr;
  }

  /**
   * Returns the expression of this Node.
   * @return Expression to return
   */
  public Node getExpression() {
    return expr;
  }
}
