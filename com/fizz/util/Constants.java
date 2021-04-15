package com.fizz.util;

import com.fizz.interpreter.value.Value;
import com.fizz.interpreter.value.Boolean;

public final class Constants {
	public static Context PROGRAM;
	public static SymbolTable GST;
	public static final String[] KEYWORDS = {
		"var", 
		"and", "or",
		"if", "else", "elif", "for", "while"
	};
	
	public static final String[] CONSTANTS = {
		"true", "false", "null"
	};
	
	public static void init() {
		GST = new SymbolTable(null);
		GST.add("true", new Boolean(true));
		GST.add("false", new Boolean(false));
		GST.add("null", new Value("null", "null"));
		
		PROGRAM = new Context("<program>", GST);
	}
	
	public static boolean isKeyword(String word) {
		for(String key : KEYWORDS) {
			if(key.equals(word)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isConstant(String id) {
		for(String con : CONSTANTS) {
			if(con.equals(id)) {
				return true;
			}
		}
		
		return false;
	}
}