package com.fizz.util.result;

import com.fizz.parser.node.Node;
import com.fizz.util.error.Error;

/**
 * Controls the passing of Nodes and Errors in the Parser, and passes along the
 * Abstract Syntax Tree to the Interpreter.
 * @author Noah James Rathman
 */
public class ParseResult extends Result<Node, ParseResult> {
  /**
   * Creates a new ParseResult.
   */
  public ParseResult() {
    super();
  }

  @Override
  public ParseResult success(Node value) {
    this.value = value;
    return this;
  }

  @Override
  public ParseResult failure(Error error) {
    this.error = error;
    return this;
  }
}
