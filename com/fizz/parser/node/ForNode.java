package com.fizz.parser.node;

/**
 * Represents a for expression.
 * @author Noah James Rathman
 */
public class ForNode extends Node {
  private Node iter, cond, step, expr;

  /**
   * Creates a new ForNode with the passed iterator, condition,
   * step and expression.
   * @param iter Iterator of Node
   * @param cond Condition of Node
   * @param step Step size of Node
   * @param expr Expression of Node
   */
  public ForNode(Node iter, Node cond, Node step, Node expr) {
    super(iter.getStart(), expr.getEnd());
    this.iter = iter;
    this.cond = cond;
    this.step = step;
    this.expr = expr;
  }

  /**
   * Returns this Node's iterator.
   * @return Iterator of Node
   */
  public Node getIterator() {
    return iter;
  }

  /**
   * Returns this Node's condition.
   * @return Condition of Node
   */
  public Node getCondition() {
    return cond;
  }

  /**
   * Returns this Node's step size.
   * @return Step size of Node
   */
  public Node getStep() {
    return step;
  }

  /**
   * Returns this Node's expression.
   * @return Expression of Node
   */
  public Node getExpression() {
    return expr;
  }
}
