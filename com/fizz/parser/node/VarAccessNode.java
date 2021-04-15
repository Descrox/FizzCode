package com.fizz.parser.node;

import com.fizz.token.Token;
import com.fizz.util.Position;

public class VarAccessNode implements Node {
	private Token varName;
	
	public VarAccessNode(Token varName) {
		this.varName = varName;
	}
	
	public Token getVarName() {
		return varName;
	}
	
	public Position getStart() {
		return varName.getStart();
	}

	public Position getEnd() {
		return varName.getEnd();
	}
	
}