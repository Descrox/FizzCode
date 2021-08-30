package com.fizz.interpreter.value;

import java.util.List;

import com.fizz.util.context.Context;
import com.fizz.util.context.SymbolTable;
import com.fizz.util.result.RuntimeResult;

/**
 * The generic Function class which holds the function and argument names.
 * @author Noah James Rathman
 */
public abstract class BaseFunc extends Val {
  /** Name of function */
  protected String name;
  /** Names of function arguments */
  protected List<String> argNames;

  /**
   * Creates a new BaseFunc with the passed argument names.
   * @param name Name of function
   * @param argNames List of argument names
   */
  public BaseFunc(String name, List<String> argNames) {
    super("Function");
    this.name = name;
    this.argNames = argNames;
  }

  /**
   * Creates a new SymbolTable with this function's argument names
   * assigned to the passed values.
   * @param argValues Values to be assigned
   * @param parent Parent SymbolTable
   * @return A new SymbolTable with argument values assigned. However,
   * if any argument fails to assign this method returns null.
   */
  protected SymbolTable populateArgs(List<Val> argValues, SymbolTable parent) {
    SymbolTable symTable = new SymbolTable(parent);

    boolean asnd = false;
    for(int i = 0; i < argValues.size(); i++) {
      asnd = symTable.set(argNames.get(i), argValues.get(i), false);
      if(!asnd) {return null;}
    }

    return symTable;
  }

  /**
   * Executes the code associated with this function.
   * @param argValues Values to be assigned
   * @param parent Context this function will extend from
   * @return Result of function execution
   */
  public abstract RuntimeResult execute(List<Val> argValues, Context parent);
}
