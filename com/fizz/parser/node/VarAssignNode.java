package com.fizz.parser.node;

import com.fizz.lexer.token.Token;

/**
 * Holds an identifier and expression for assignment to a SymbolTable.
 * @author Noah James Rathman
 */
public class VarAssignNode extends Node {
  private Token name;
  private Node expr;
  private boolean constant;
  private byte type;

  /**
   * Creates a new VarAssignNode with the passed name, expression
   * and constant state.
   * @param name Name of variable
   * @param expr Expression of variable
   * @param constant Constant state of variable
   * @param type Type of assignment
   */
  public VarAssignNode(Token name, Node expr, boolean constant, byte type) {
    super(name.getStart(), expr.getEnd());
    this.name = name;
    this.expr = expr;
    this.constant = constant;
    this.type = type;
  }

  /**
   * Returns the name held by this Node.
   * @return Varaible name to be assigned
   */
  public Token getName() {
    return name;
  }

  /**
   * Returns the expression held by this Node.
   * @return Varaible value to be assigned
   */
  public Node getExpr() {
    return expr;
  }

  /**
   * Returns the constant state held by this Node.
   * @return Constant state of variable
   */
  public boolean isConstant() {
    return constant;
  }

  /**
   * Returns the byte representation of this Node's
   * assignment type.
   * @return Node assign type
   */
  public byte getType() {
    return type;
  }
}
