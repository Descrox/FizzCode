package com.fizz.interpreter.value;

import com.fizz.interpreter.RuntimeResult;
import com.fizz.util.error.RuntimeError;

public class Boolean extends Value {
	public Boolean(Object value) {
		super(value, "Boolean");
	}
	
	public RuntimeResult and(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		if(other.getType().equals("Boolean")) {
			Boolean result = new Boolean((boolean) value && (boolean) other.getValue());
			result.setPosition(start, other.getEnd());
			result.setContext(other.getContext());
			
			return res.success(result);
		}
		
		return res.failure(new RuntimeError(
			other.getType() + "s cannot be anded with Booleans.",
			other.getStart(), other.getEnd(), other.getContext()
		));
	}
	
	public RuntimeResult or(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		if(other.getType().equals("Boolean")) {
			Boolean result = new Boolean((boolean) value || (boolean) other.getValue());
			result.setPosition(start, other.getEnd());
			result.setContext(other.getContext());
			
			return res.success(result);
		}
		
		return res.failure(new RuntimeError(
			other.getType() + "s cannot be ored with Booleans.",
			other.getStart(), other.getEnd(), other.getContext()
		));
	}
	
	public RuntimeResult not() {
		RuntimeResult res = new RuntimeResult();
		
		Boolean bool = new Boolean(!(boolean) value);
		bool.setPosition(start, end);
		bool.setContext(context);
		
		return res.success(bool);
	}
}