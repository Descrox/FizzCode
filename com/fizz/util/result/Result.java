package com.fizz.util.result;

import com.fizz.util.error.Error;

/**
 * A baseplate for both ParseResult and RuntimeResult, which both manage errors
 * and values passed through their respective call classes.
 * @param <A> The type of value that the Result holds
 * @param <B> The type of Result returned by {@link #success(A)} and {@link #failure(Error)}
 * @author Noah James Rahtman
 */
public abstract class Result<A, B> {
  /**
   * Value held by the Result when {@link #success(A)} is called.
   */
  protected A value;
  /**
   * Error held by the Result when {@link #failure(Error)} is called.
   */
  protected Error error;

  /**
   * Creates a new Result with its value and Error set to {@code null}.
   * Result should only be instanced in its subclasses, so this constructor
   * is protected.
   */
  protected Result() {
    value = null;
    error = null;
  }

  /**
   * Registers the passed Result with the same value type.
   * If the passed Result has an Error, this Result's error is
   * set to that.
   * @param res Result to be registered
   * @return Passed Result value
   */
  public A register(Result<A, B> res) {
    if(res.hasError()) {
      error = res.getError();
    }

    return res.getValue();
  }

  /**
   * Called when this Result hasn't already failed. Sets the value of this
   * Result to the passed value.
   * @param value Successful Result value
   * @return This method returns itself
   */
  public abstract B success(A value);

  /**
   * Called when this Result runs into an Error. Sets the Error of this
   * Result to the passed Error.
   * @param error Error caught by Result
   * @return This method returns itself
   */
  public abstract B failure(Error error);

  /**
   * Returns the value of this Result.
   * @return Result value
   */
  public A getValue() {
    return value;
  }

  /**
   * Returns the Error of this Result.
   * @return Result Error
   */
  public Error getError() {
    return error;
  }

  /**
   * Returns if this Result has an Error.
   * @return True if this Result's Error isn't null
   */
  public boolean hasError() {
    return error != null;
  }
}
