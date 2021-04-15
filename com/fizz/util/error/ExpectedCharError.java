package com.fizz.util.error;

import com.fizz.util.Position;

public class ExpectedCharError extends Error {
	public ExpectedCharError(String details, Position start, Position end) {
		super("Expected Character", details, start, end);
	}
}