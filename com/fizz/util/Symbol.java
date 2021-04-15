package com.fizz.util;

import com.fizz.interpreter.value.Value;

public class Symbol {
	private String name;
	private Value value;
	
	public Symbol(String name, Value value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public Value getValue() {
		return value;
	}
	
	public void setValue(Value value) {
		this.value = value;
	}
}