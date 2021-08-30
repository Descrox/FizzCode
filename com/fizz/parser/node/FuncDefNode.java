package com.fizz.parser.node;

import java.util.List;

import com.fizz.lexer.token.Token;

/**
 * Represents the definition of a function.
 * @author Noah James Rathman
 */
public class FuncDefNode extends Node {
  private Token name;
  private List<String> argNames;
  private Node statement;

  /**
   * Creates a new FuncDefNode with the passed name, argument names
   * and statement.
   * @param name Function name
   * @param argNames Function argument names
   * @param statement Function statement
   */
  public FuncDefNode(Token name, List<String> argNames, Node statement) {
    super(name.getStart(), statement.getEnd());
    this.name = name;
    this.argNames = argNames;
    this.statement = statement;
  }

  /**
   * Returns the function's name.
   * @return Function's defined name
   */
  public String getName() {
    return name.getValue();
  }

  /**
   * Returns the function's argument names.
   * @return Function's defined argument names
   */
  public List<String> getArgNames() {
    return argNames;
  }

  /**
   * Returns the function's statement
   * @return Function's defined statement
   */
  public Node getStatement() {
    return statement;
  }
}
