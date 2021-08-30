package com.fizz.util.function;

import com.fizz.parser.node.Node;
import com.fizz.util.context.Context;
import com.fizz.util.result.RuntimeResult;

/**
 * Used for the passing and calling of methods in the Interpreter.
 * @author Noah James Rathman
 */
@FunctionalInterface
public interface IFunc {
  /**
   * Calls the assigned Interpreter method.
   * @param _node Node to be interpreter
   * @param context Current Context of Interpreter
   * @return Result of method execution
   */
  RuntimeResult run(Node _node, Context context);
}
