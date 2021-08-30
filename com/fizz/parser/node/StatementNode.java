package com.fizz.parser.node;

import java.util.List;

/**
 * Represents a multi-line statement.
 * @author Noah James Rathman
 */
public class StatementNode extends Node {
  private List<Node> statements;

  /**
   * Creates a new StatementNode with the passed List of Nodes.
   * @param statements All Nodes included in this statement
   */
  public StatementNode(List<Node> statements) {
    super(
      statements.get(0).getStart(),
      statements.get(statements.size() - 1).getEnd()
    );

    this.statements = statements;
  }

  /**
   * Returns all statements held in this Node.
   * @return List of statements
   */
  public List<Node> getStatements() {
    return statements;
  }
}
