package com.fizz.util;

import java.util.ArrayList;

import com.fizz.interpreter.RuntimeResult;
import com.fizz.interpreter.value.Value;
import com.fizz.util.error.RuntimeError;

public class SymbolTable {
	private SymbolTable parent;
	private ArrayList<Symbol> symbols;
	
	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
		symbols = new ArrayList<Symbol>();
	}
	
	public void add(String name, Value value) {
		Symbol sym = new Symbol(name, value);
		int index = indexTo(name);
		
		if(index == -1) {
			symbols.add(sym);
		} else {
			symbols.get(index).setValue(value);
		}
	}
	
	public Value get(String name) {
		RuntimeResult res = new RuntimeResult();
		int index = indexTo(name);
		
		if(index == -1) {
			if(parent != null) {
				return parent.get(name);
			}
			
			return null;
		} else {
			return symbols.get(index).getValue();
		}
	}
	
	public void remove(String name) {
		for(int i = 0; i < symbols.size(); i++) {
			Symbol sym = symbols.get(i);
			
			if(sym.getName().equals(name)) {
				symbols.remove(i);
				return;
			}
		}
	}
	
	public int indexTo(String name) {
		for(int i = 0; i < symbols.size(); i++) {
			if(symbols.get(i).getName().equals(name)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public boolean contains(String name) {
		for(Symbol sym : symbols) {
			if(sym.getName().equals(name)) {
				return true;
			}
		}
		
		if(parent != null) {
			return parent.contains(name);
		}
		
		return false;
	}
}