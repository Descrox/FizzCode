package com.fizz.parser.node;

import java.util.List;

/**
 * Represents an if expression.
 * @author Noah James Rathman
 */
public class IfNode extends Node {
  private List<Node> conds;
  private List<Node> exprs;
  private Node elseCase;

  /**
   * Creates a new IfNode with the passed conditions, expressions
   * and else case.
   * @param conds Conditions of if expression
   * @param exprs Expressions of if expression
   * @param elseCase Else case of if expression
   */
  public IfNode(List<Node> conds, List<Node> exprs, Node elseCase) {
    super(
      conds.get(0).getStart(),
      elseCase != null ? elseCase.getEnd() : exprs.get(exprs.size() - 1).getEnd()
    );

    this.conds    = conds;
    this.exprs    = exprs;
    this.elseCase = elseCase;
  }

  /**
   * Returns this Node's List of conditions.
   * @return If statement conditions
   */
  public List<Node> getConditions() {
    return conds;
  }

  /**
   * Returns this Node's List of expressions.
   * @return If statement expressions
   */
  public List<Node> getExpressions() {
    return exprs;
  }

  /**
   * Returns this Node's else case.
   * @return If statement else case
   */
  public Node getElseCase() {
    return elseCase;
  }
}
