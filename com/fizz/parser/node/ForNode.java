package com.fizz.parser.node;

import com.fizz.util.Position;

public class ForNode implements Node {
	private Node iterator, condition, step, expression;
	
	public ForNode(Node iterator, Node condition, Node step, Node expression) {
		this.iterator = iterator;
		this.condition = condition;
		this.step = step;
		this.expression = expression;
	}
	
	public Node getIterator() {
		return iterator;
	}
	
	public Node getCondition() {
		return condition;
	}
	
	public Node getStep() {
		return step;
	}
	
	public Node getExpression() {
		return expression;
	}
	
	public Position getStart() {
		return iterator.getStart();
	}

	public Position getEnd() {
		return expression.getEnd();
	}
}