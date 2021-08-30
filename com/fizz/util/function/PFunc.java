package com.fizz.util.function;

import com.fizz.util.result.ParseResult;

/**
 * Used for the passing and calling of methods in the Parser.
 * @author Noah James Rathman
 */
@FunctionalInterface
public interface PFunc {
  /**
   * Calls the assigned Parser method.
   * @return Result of method execution
   */
  ParseResult run();
}
