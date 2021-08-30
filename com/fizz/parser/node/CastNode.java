package com.fizz.parser.node;

import com.fizz.lexer.token.Token;

/**
 * Represents a cast from one value to another.
 * @author Noah James Rathman
 */
public class CastNode extends Node {
  private Token cast;
  private Node expr;

  /**
   * Creates a new CastNode with the passed cast and expression.
   * @param cast What the expression is casting to
   * @param expr Cast expression
   */
  public CastNode(Token cast, Node expr) {
    super(cast.getStart(), expr.getEnd());
    this.cast = cast;
    this.expr = expr;
  }

  /**
   * Returns the cast type of this Node.
   * @return Node cast
   */
  public Token getCast() {
    return cast;
  }

  /**
   * Returns the expression of this Node.
   * @return Node expression
   */
  public Node getExpression() {
    return expr;
  }
}
