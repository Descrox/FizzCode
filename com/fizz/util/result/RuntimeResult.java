package com.fizz.util.result;

import com.fizz.interpreter.value.Val;
import com.fizz.util.error.Error;

/**
 * Controls the passing of Vals and Errors in the Interpreter, and passes
 * along the final result of the Abstract Syntax Tree.
 * @author Noah James Rathman
 */
public class RuntimeResult extends Result<Val, RuntimeResult> {
  /**
   * Creates a new RuntimeResult.
   */
  public RuntimeResult() {
    super();
  }

  @Override
  public RuntimeResult success(Val value) {
    this.value = value;
    return this;
  }

  @Override
  public RuntimeResult failure(Error error) {
    this.error = error;
    return this;
  }
}
