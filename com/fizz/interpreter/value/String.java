package com.fizz.interpreter.value;

import com.fizz.interpreter.RuntimeResult;

public class String extends Value {
	public String(Object value) {
		super(value, "String");
	}
	
	public RuntimeResult add(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		String result = new String(value.toString() + other.getValue());
		result.setPosition(start, other.getEnd());
		result.setContext(other.getContext());
		
		return res.success(result);
	}
}