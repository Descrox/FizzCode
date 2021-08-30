package com.fizz.parser.node;

import java.util.List;

import com.fizz.lexer.token.Token;

/**
 * Represents a call to a function.
 * @author Noah James Rathman
 */
public class CallNode extends Node {
  private Token name;
  private List<Node> argValues;

  /**
   * Creates a new CallNode with the passed name and argument values.
   * @param name Name of function
   * @param argValues Function argument values
   */
  public CallNode(Token name, List<Node> argValues) {
    super(name.getStart(), argValues.size() > 0 ? argValues.get(argValues.size() - 1).getEnd() : name.getEnd());
    this.name = name;
    this.argValues = argValues;
  }

  /**
   * Returns the name of the called function.
   * @return Name of function
   */
  public Token getName() {
    return name;
  }

  /**
   * Returns the argument values of the function
   * @return Function argument values
   */
  public List<Node> getArgValues() {
    return argValues;
  }
}
