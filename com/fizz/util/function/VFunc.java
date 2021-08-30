package com.fizz.util.function;

import com.fizz.util.result.RuntimeResult;
import com.fizz.interpreter.value.Val;

/**
 * Used for the passing and calling of methods in value classes.
 * @author Noah James Rathman
 */
@FunctionalInterface
public interface VFunc {
  /**
   * Calls the assigned value method.
   * @param self Value which called this function
   * @param other Value being operated on self
   * @return Result of method execution
   */
  RuntimeResult run(Val self, Val other);
}
