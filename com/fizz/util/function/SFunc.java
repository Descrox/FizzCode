package com.fizz.util.function;

import com.fizz.util.Position;
import com.fizz.util.result.RuntimeResult;
import com.fizz.util.context.Context;

/**
 * Represents functions called inside of
 * {@link com.fizz.interpreter.value.SysFunc}.
 * @author Noah James Rathman
 */
@FunctionalInterface
public interface SFunc {
  /**
   * Runs the assigned function.
   * @param entry Position of entry into function
   * @param context Context to be used in function
   * @return Result of function execution
   */
  RuntimeResult run(Position entry, Context context);
}
