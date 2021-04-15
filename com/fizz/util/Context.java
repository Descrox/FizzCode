package com.fizz.util;

public class Context {
	private String name;
	private Context parent;
	private Position parentEntryPos;
	private SymbolTable symTable;
	
	public Context(String name, SymbolTable symTable) {
		this.name = name;
		this.parent = null;
		this.parentEntryPos = null;
		this.symTable = symTable;
	}
	
	public Context(String name, Context parent, Position parentEntryPos, SymbolTable symTable) {
		this.name = name;
		this.parent = parent;
		this.parentEntryPos = parentEntryPos;
		this.symTable = symTable;
	}
	
	public String getName() {
		return name;
	}
	
	public Context getParent() {
		return parent;
	}
	
	public Position getParentEntryPosition() {
		return parentEntryPos;
	}
	
	public SymbolTable getSymbolTable() {
		return symTable;
	}
}