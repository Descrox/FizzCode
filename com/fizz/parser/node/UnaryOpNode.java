package com.fizz.parser.node;

import com.fizz.lexer.token.Token;
import com.fizz.util.Position;

/**
 * Represents an operation on a single Node.
 * @author Noah James Rathman
 */
public class UnaryOpNode extends Node {
  private Token op;
  private Node node;

  /**
   * Creates a new UnaryOpNode with the passed operation and Node.
   * @param op Operation Token
   * @param node Operation Node
   */
  public UnaryOpNode(Token op, Node node) {
    super(op.getStart(), node.getEnd());
    this.op = op;
    this.node = node;
  }

  /**
   * Returns the operation Token of this Node.
   * @return Operation Token
   */
  public Token getOp() {
    return op;
  }

  /**
   * Returns the operation Node of this Node.
   * @return Operation Node
   */
   public Node getNode() {
     return node;
   }

   public String toString() {
     return "(" + op + node + ")";
   }
}
