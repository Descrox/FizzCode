package com.fizz.interpreter;

import java.util.ArrayList;

import com.fizz.interpreter.value.Boolean;
import com.fizz.interpreter.value.List;
import com.fizz.interpreter.value.Number;
import com.fizz.interpreter.value.Value;
import com.fizz.interpreter.value.String;
import com.fizz.parser.node.BinOpNode;
import com.fizz.parser.node.ForNode;
import com.fizz.parser.node.IfNode;
import com.fizz.parser.node.ListNode;
import com.fizz.parser.node.Node;
import com.fizz.parser.node.NumberNode;
import com.fizz.parser.node.StringNode;
import com.fizz.parser.node.UnaryOpNode;
import com.fizz.parser.node.VarAccessNode;
import com.fizz.parser.node.VarAssignNode;
import com.fizz.parser.node.WhileNode;
import com.fizz.token.TokenType;
import com.fizz.util.Constants;
import com.fizz.util.Context;
import com.fizz.util.error.RuntimeError;

/**
 * Interprets the passed abstract syntax tree generated in the Parser.
 * @author Noah James Rathman
 */
public class Interpreter {
	/**
	 * Visits the passed Node.
	 * @param node		Node to be interpreted
	 * @param context	Context of Node
	 * @return Result of Node interpretation
	 */
	public static RuntimeResult visit(Node node, Context context) {
		if(node instanceof NumberNode) {
			return visitNumberNode((NumberNode) node, context); 
		} else if(node instanceof BinOpNode) {
			return visitBinOpNode((BinOpNode) node, context);
		} else if(node instanceof UnaryOpNode) {
			return visitUnaryOpNode((UnaryOpNode) node, context);
		} else if(node instanceof VarAssignNode) {
			return visitVarAssignNode((VarAssignNode) node, context);
		} else if(node instanceof VarAccessNode) {
			return visitVarAccessNode((VarAccessNode) node, context);
		} else if(node instanceof IfNode) {
			return visitIfNode((IfNode) node, context);
		} else if(node instanceof WhileNode) {
			return visitWhileNode((WhileNode) node, context);
		} else if(node instanceof ForNode) {
			return visitForNode((ForNode) node, context);
		} else if(node instanceof ListNode) {
			return visitListNode((ListNode) node, context);
		} else if(node instanceof StringNode) {
			return visitStringNode((StringNode) node, context);
		}
		
		return noVisitMethod(node, context);
	}
	
	/**
	 * Called when the passed Node has no defined visit method.
	 * @param node		Undefined Node
	 * @param context	Context of Node
	 * @return Result of Visit
	 */
	private static RuntimeResult noVisitMethod(Node node, Context context) {
		RuntimeResult res = new RuntimeResult();
		return res.failure(new RuntimeError(
				"No visit method defined for " + node.getClass().getSimpleName() + ".",
				node.getStart(), node.getEnd(), context
		));
	}
	
	/**
	 * Called when the passed Node is a NumberNode.
	 * @param node		NumberNode node
	 * @param context	Context of Node
	 * @return Result of Visit
	 */
	private static RuntimeResult visitNumberNode(NumberNode node, Context context) {
		RuntimeResult res = new RuntimeResult();
		Number num = new Number(node.getValue().getValue());
		num.setPosition(node.getStart(), node.getEnd());
		num.setContext(context);
		
		return res.success(num);
	}
	
	private static RuntimeResult visitBinOpNode(BinOpNode node, Context context) {
		RuntimeResult res = new RuntimeResult();
		Value left = res.register(visit(node.getLeftNode(), context));
		if(res.getError() != null) {return res;}
		
		Value right = res.register(visit(node.getRightNode(), context));
		if(res.getError() != null) {return res;}
		
		Value result = null;
		if(left.getType().equals("Number")) {
			if(node.getOperation().isType(TokenType.ADD)) {
				result = res.register(((Number) left).add(right));
			} else if(node.getOperation().isType(TokenType.SUB)) {
				result = res.register(((Number) left).subtract(right));
			} else if(node.getOperation().isType(TokenType.MUL)) {
				result = res.register(((Number) left).multiply(right));
			} else if(node.getOperation().isType(TokenType.DIV)) {
				result = res.register(((Number) left).divide(right));
			} else if(node.getOperation().isType(TokenType.EE)) {
				result = res.register(((Number) left).equalTo(right));
			} else if(node.getOperation().isType(TokenType.NE)) {
				result = res.register(((Number) left).notEqualTo(right));
			} else if(node.getOperation().isType(TokenType.LT)) {
				result = res.register(((Number) left).lessThan(right));
			} else if(node.getOperation().isType(TokenType.GT)) {
				result = res.register(((Number) left).greaterThan(right));
			} else if(node.getOperation().isType(TokenType.LTE)) {
				result = res.register(((Number) left).lessThanOrEqualTo(right));
			} else if(node.getOperation().isType(TokenType.GTE)) {
				result = res.register(((Number) left).greaterThanOrEqualTo(right));
			} else {
				return res.failure(new RuntimeError(
						"Unsupported operation \"" + node.getOperation() + "\" for a Number.",
						node.getOperation().getStart(), node.getOperation().getEnd(), context
				));
			}
			
			if(res.getError() != null) {return res;}
			
			return res.success(result);
		} else if(left.getType().equals("Boolean")) {
			if(node.getOperation().matches(TokenType.KEY, new java.lang.String[] {"and"})) {
				result = res.register(((Boolean) left).and(right));
			} else if(node.getOperation().matches(TokenType.KEY, new java.lang.String[] {"or"})) {
				result = res.register(((Boolean) left).or(right));
			} else {
				return res.failure(new RuntimeError(
						"Unsupported operation \"" + node.getOperation() + "\" for a Boolean.",
						node.getOperation().getStart(), node.getOperation().getEnd(), context
				));
			}
			
			if(res.getError() != null) {return res;}
			
			return res.success(result);
		} else if(left.getType().equals("String")) {
			if(node.getOperation().isType(TokenType.ADD)) {
				result = res.register(((String) left).add(right));
			} else {
				return res.failure(new RuntimeError(
						"Unsupported operation \"" + node.getOperation() + "\" for a String.",
						node.getOperation().getStart(), node.getOperation().getEnd(), context
				));
			}
			
			if(res.getError() != null) {return res;}
			
			return res.success(result);
		} else {
			return res.failure(new RuntimeError(
					left.getType() + "s cannot be used in a binary operation.",
					left.getStart(), left.getEnd(), context
			));
		}
	}
	
	private static RuntimeResult visitUnaryOpNode(UnaryOpNode node, Context context) {
		RuntimeResult res = new RuntimeResult();
		
		Value val = res.register(visit(node.getValue(), context));
		if(res.getError() != null) {return res;}
		
		Value result = null;
		if(val.getType().equals("Number")) {
			if(node.getOperation().isType(TokenType.SUB)) {
				result = res.register(((Number) val).multiply(new Number(-1)));
			} else {
				return res.failure(new RuntimeError(
						"Unsupported operation \"" + node.getOperation() + "\" for a Number.",
						node.getOperation().getStart(), node.getOperation().getEnd(), context
				));
			}
			
			if(res.getError() != null) {return res;}
			
			result.setPosition(node.getStart(), node.getEnd());
			result.setContext(context);
			
			return res.success(result);
		} else if(val.getType().equals("Boolean")) {
			if(node.getOperation().isType(TokenType.NOT)) {
				result = res.register(((Boolean) val).not());
			} else {
				return res.failure(new RuntimeError(
						"Unsupported operation \"" + node.getOperation() + "\" for a Boolean.",
						node.getOperation().getStart(), node.getOperation().getEnd(), context
				));
			}
			
			if(res.getError() != null) {return res;}
			
			result.setPosition(node.getStart(), node.getEnd());
			result.setContext(context);
			
			return res.success(result);
		} else {
			return res.failure(new RuntimeError(
					val.getType() + "s cannot be used in a unary operation.",
					val.getStart(), val.getEnd(), context
			));
		}
	}
	
	private static RuntimeResult visitVarAssignNode(VarAssignNode node, Context context) {
		RuntimeResult res = new RuntimeResult();
		final java.lang.String varName = node.getName().getValue().toString();
		
		if(Constants.isConstant(varName)) {
			return res.failure(new RuntimeError(
				"Variable \"" + varName + "\" is constant and cannot be changed.",
				node.getStart(), node.getEnd(), context
			));
		}
		
		Value varVal = res.register(visit(node.getValue(), context));
		if(res.getError() != null) {return res;}
		
		varVal.setPosition(node.getStart(), node.getEnd());
		varVal.setContext(context);
		context.getSymbolTable().add(varName, varVal);
		
		Value nullVal = Constants.GST.get("null");
		nullVal.setPosition(node.getStart(), node.getEnd());
		nullVal.setContext(context);
		
		return res.success(nullVal);
	}
	
	private static RuntimeResult visitVarAccessNode(VarAccessNode node, Context context) {
		RuntimeResult res = new RuntimeResult();
		final java.lang.String varName = node.getVarName().getValue().toString();
		
		Value varVal = context.getSymbolTable().get(varName);
		
		if(varVal == null) {
			return res.failure(new RuntimeError(
				"Varaible \"" + varName + "\" does not exist.",
				node.getStart(), node.getEnd(), context
			));
		}
		
		varVal.setPosition(node.getStart(), node.getEnd());
		varVal.setContext(context);
		
		return res.success(varVal);
	}
	
	private static RuntimeResult visitIfNode(IfNode node, Context context) {
		RuntimeResult res = new RuntimeResult();
		
		boolean condTrue = false;
		for(int i = 0; i < node.getConditions().size(); i++) {
			Node cond = node.getConditions().get(i);
			Node expr = node.getExpressions().get(i);
			
			Value conVal = res.register(visit(cond, context));
			if(res.getError() != null) {return res;}
			
			if(!conVal.getType().equals("Boolean")) {
				return res.failure(new RuntimeError(
					"Conditionals must return a Boolean value.",
					conVal.getStart(), conVal.getEnd(), context
				));
			}
			
			if(conVal.getValue().equals(true)) {
				res.register(visit(expr, context));
				if(res.getError() != null) {return res;}
				condTrue = true;
				break;
			}
		}
		
		if(node.getElseCase() != null && !condTrue) {
			res.register(visit(node.getElseCase(), context));
			if(res.getError() != null) {return res;}
		}
		
		Value nullVal = Constants.GST.get("null");
		nullVal.setPosition(node.getStart(), node.getEnd());
		nullVal.setContext(context);
		
		return res.success(nullVal);
	}
	
	private static RuntimeResult visitWhileNode(WhileNode node, Context context) {
		RuntimeResult res = new RuntimeResult();
		Value cond = res.register(visit(node.getCondition(), context));
		if(res.getError() != null) {return res;}
		
		if(!cond.getType().equals("Boolean")) {
			return res.failure(new RuntimeError(
				"Conditionals must return a Boolean value.",
				cond.getStart(), cond.getEnd(), context
			));
		}
		
		while(cond.getValue().equals(true)) {
			res.register(visit(node.getExpression(), context));
			if(res.getError() != null) {return res;}
			
			cond = res.register(visit(node.getCondition(), context));
			if(res.getError() != null) {return res;}
		}
		
		Value nullVal = Constants.GST.get("null");
		nullVal.setPosition(node.getStart(), node.getEnd());
		nullVal.setContext(context);
		
		return res.success(nullVal);
	}
	
	private static RuntimeResult visitForNode(ForNode node, Context context) {
		RuntimeResult res = new RuntimeResult();
		final java.lang.String varName = ((VarAssignNode) node.getIterator()).getName().getValue().toString();

		res.register(visit(node.getIterator(), context));
		if(res.getError() != null) {return res;}
		
		if(!context.getSymbolTable().get(varName).getType().equals("Number")) {
			return res.failure(new RuntimeError(
				"Iterator must be a Number value.",
				node.getIterator().getStart(), node.getIterator().getEnd(), context
			));
		}
		
		Value cond = res.register(visit(node.getCondition(), context));
		if(res.getError() != null) {return res;}
		
		if(!cond.getType().equals("Boolean")) {
			return res.failure(new RuntimeError(
				"Conditionals must return a Boolean value.",
				cond.getStart(), cond.getEnd(), context
			));
		}
		
		Value step;
		if(node.getStep() != null) {
			step = res.register(visit(node.getStep(), context));
			if(res.getError() != null) {return res;}
			
			if(!step.getType().equals("Number")) {
				return res.failure(new RuntimeError(
					"Step must be a Number value.",
					step.getStart(), step.getEnd(), context
				));
			}
		} else {
			step = new Number(1);
		}
		
		while(cond.getValue().equals(true)) {
			res.register(visit(node.getExpression(), context));
			if(res.getError() != null) {return res;}
			
			Value oldIter = context.getSymbolTable().get(varName);
			Value newIter = res.register(((Number) oldIter).add(step));
			if(res.getError() != null) {return res;}
			
			context.getSymbolTable().add(varName, newIter);
			
			cond = res.register(visit(node.getCondition(), context));
			if(res.getError() != null) {return res;}
		}
		
		context.getSymbolTable().remove(varName);
		
		Value nullVal = Constants.GST.get("null");
		nullVal.setPosition(node.getStart(), node.getEnd());
		nullVal.setContext(context);
		
		return res.success(nullVal);
	}
	
	private static RuntimeResult visitListNode(ListNode node, Context context) {
		RuntimeResult res = new RuntimeResult();
		ArrayList<Value> values = new ArrayList<Value>();
		java.lang.String valType = null;
		
		for(Node lVal : node.getValues()) {
			Value v = res.register(visit(lVal, context));
			if(res.getError() != null) {return res;}
			
			if(valType == null) {
				valType = v.getType();
			} else {
				if(!v.getType().equals(valType)) {
					return res.failure(new RuntimeError(
						"Expected a " + valType + ".",
						v.getStart(), v.getEnd(), context
					));
				}
			}
			
			values.add(v);
		}
		
		List lVal = new List(values);
		lVal.setPosition(node.getStart(), node.getEnd());
		lVal.setContext(context);
		
		return res.success(lVal);
	}
	
	private static RuntimeResult visitStringNode(StringNode node, Context context) {
		RuntimeResult res = new RuntimeResult();
		
		String val = new String(node.getValue().getValue());
		val.setPosition(node.getStart(), node.getEnd());
		val.setContext(context);
		
		return res.success(val);
	}
}