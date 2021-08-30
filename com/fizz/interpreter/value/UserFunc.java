package com.fizz.interpreter.value;

import java.util.List;

import com.fizz.interpreter.Interpreter;
import com.fizz.parser.node.Node;
import com.fizz.util.context.Context;
import com.fizz.util.context.SymbolTable;
import com.fizz.util.result.RuntimeResult;
import com.fizz.util.error.RuntimeError;

/**
 * Holds information for functions defined by the user.
 * @author Noah James Rathman
 */
public class UserFunc extends BaseFunc {
  private Node statement;

  /**
   * Creates a new UserFunc with the passed name, argument names
   * and statement.
   * @param name Name of function
   * @param argNames Names of function arguments
   * @param statement Statement of function
   */
  public UserFunc(String name, List<String> argNames, Node statement) {
    super(name, argNames);
    this.statement = statement;
  }

  @Override
  public RuntimeResult execute(List<Val> argValues, Context parent) {
    RuntimeResult res = new RuntimeResult();
    if(argNames.size() != argValues.size()) {
      return res.failure(RuntimeError.argumentSize(argNames.size(), argValues.size(), start, end, parent));
    }

    SymbolTable fSymTable = populateArgs(argValues, parent.getSymbolTable());
    if(fSymTable == null) {
      return res.failure(RuntimeError.tableCreation(start, end, parent));
    }
    Context fContext = new Context("<Function " + name + ">", fSymTable, parent, start);

    Val result = res.register(Interpreter.visit(statement, fContext));
    if(res.hasError()) {return res;}

    return res.success(result);
  }

  public String toString() {
    return "<User Function: " + name + ">";
  }
}
