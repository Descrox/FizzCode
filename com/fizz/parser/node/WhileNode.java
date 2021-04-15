package com.fizz.parser.node;

import com.fizz.util.Position;

public class WhileNode implements Node {
	private Node condition;
	private Node expression;
	
	public WhileNode(Node condition, Node expression) {
		this.condition = condition;
		this.expression = expression;
	}
	
	public Node getCondition() {
		return condition;
	}
	
	public Node getExpression() {
		return expression;
	}
	
	public Position getStart() {
		return condition.getStart();
	}

	public Position getEnd() {
		return expression.getEnd();
	}

}