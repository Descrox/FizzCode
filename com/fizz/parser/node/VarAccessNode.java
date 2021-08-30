package com.fizz.parser.node;

import com.fizz.lexer.token.Token;

/**
 * Holds an identifier which is used to access Symbols in the SymbolTable.
 * @author Noah James Rathman
 */
public class VarAccessNode extends Node {
  private Token name;

  /**
   * Creates a new VarAccessNode with the passed name.
   * @param name Name of variable
   */
  public VarAccessNode(Token name) {
    super(name.getStart(), name.getEnd());
    this.name = name;
  }

  /**
   * Returns the name held by this Node.
   * @return Varaible name to be accessed
   */
  public Token getName() {
    return name;
  }
}
