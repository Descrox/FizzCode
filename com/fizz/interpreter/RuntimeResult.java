package com.fizz.interpreter;

import com.fizz.interpreter.value.Value;
import com.fizz.util.error.RuntimeError;

/**
 * Used for authentication of results in the Lexer.
 * @author Noah James Rathman
 */
public class RuntimeResult {
	private Value result;
	private RuntimeError error;
	
	/**
	 * Creates a new RuntimeResult, setting the result and error values
	 * to null.
	 */
	public RuntimeResult() {
		this.result = null;
		this.error = null;
	}
	
	/**
	 * Returns the result of this RuntimeResult.
	 * @return RuntimeResult result
	 */
	public Value getResult() {
		return result;
	}
	
	/**
	 * Returns the error of this RuntimeResult.
	 * @return RuntimeResult error
	 */
	public RuntimeError getError() {
		return error;
	}
	
	/**
	 * Registers the passed RuntimeResult. This result's error is set to the passed's error,
	 * and the passed's result is returned.
	 * @param other RuntimeResult to be registered
	 * @return Registered value
	 */
	public Value register(RuntimeResult other) {
		this.error = other.getError();
		return other.getResult();
	}
	
	/**
	 * Assigns this RuntimeResult's result to the passed Value, and then returns this
	 * RuntimeResult.
	 * @param result Successful Value
	 * @return Successful RuntimeResult
	 */
	public RuntimeResult success(Value result) {
		this.result = result;
		return this;
	}
	
	/**
	 * Assigns this RuntimeResult's error to the passed RutnimeError, and then returns this
	 * RuntimeResult.
	 * @param result Failed RuntimeError
	 * @return Failed RuntimeResult
	 */
	public RuntimeResult failure(RuntimeError error) {
		this.error = error;
		return this;
	}
}