package com.fizz.parser.node;

import java.util.ArrayList;

import com.fizz.util.Position;

public class IfNode implements Node {
	private ArrayList<Node> conditions;
	private ArrayList<Node> expressions;
	private Node elseCase;
	
	public IfNode(ArrayList<Node> conditions, ArrayList<Node> expressions, Node elseCase) {
		this.conditions = conditions;
		this.expressions = expressions;
		this.elseCase = elseCase;
	}
	
	public ArrayList<Node> getConditions() {
		return conditions;
	}
	
	public ArrayList<Node> getExpressions() {
		return expressions;
	}
	
	public Node getElseCase() {
		return elseCase;
	}
	
	public Position getStart() {
		return conditions.get(0).getStart();
	}

	public Position getEnd() {
		if(elseCase != null) {
			return elseCase.getEnd();
		}
		
		return expressions.get(expressions.size() - 1).getEnd();
	}
}