package com.fizz.parser.node;

import com.fizz.lexer.token.Token;

/**
 * Represents an operation between two Nodes.
 * @author Noah James Rathman
 */
public final class BinOpNode extends Node {
  private Node left, right;
  private Token op;

  /**
   * Creates a new BinOpNode with the passed operation and nodes.
   * @param left Left operation Node
   * @param op Operation Token
   * @param right Right operation Node
   */
  public BinOpNode(Node left, Token op, Node right) {
    super(left.getStart(), right.getEnd());
    this.left = left;
    this.op = op;
    this.right = right;
  }

  /**
   * Returns the left Node of this BinOpNode.
   * @return Left Node
   */
  public Node getLeftNode() {
    return left;
  }

  /**
   * Returns the operation of this BinOpNode.
   * @return Operation
   */
  public Token getOp() {
    return op;
  }

  /**
   * Returns the right Node of this BinOpNode.
   * @return Right Node
   */
  public Node getRightNode() {
    return right;
  }

  public String toString() {
    return "(" + left + " " + op + " " + right + ")";
  }
}
