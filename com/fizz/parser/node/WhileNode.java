package com.fizz.parser.node;

/**
 * Represents a while expression.
 * @author Noah James Rathman
 */
public class WhileNode extends Node {
  private Node cond;
  private Node expr;

  /**
   * Creates a new WhileNode with the passed condition and expression.
   * @param cond Condition of Node
   * @param expr Expression of Node
   */
  public WhileNode(Node cond, Node expr) {
    super(cond.getStart(), expr.getEnd());
    this.cond = cond;
    this.expr = expr;
  }

  /**
   * Returns this Node's condition.
   * @return Condition of Node
   */
  public Node getCondition() {
    return cond;
  }

  /**
   * Returns this Node's expression.
   * @return Expression of Node
   */
  public Node getExpression() {
    return expr;
  }
}
