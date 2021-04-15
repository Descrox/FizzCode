package com.fizz.parser;

import java.util.ArrayList;

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
import com.fizz.token.Token;
import com.fizz.token.TokenType;
import com.fizz.util.Position;
import com.fizz.util.error.InvalidSyntaxError;

/**
 * Parses the created Array of Tokens into an Abstract Syntax Tree.
 * @author Noah James Rathman
 */
public class Parser {
	private static ArrayList<Token> tokens;
	private static Token curTok;
	private static int pos;
	
	/**
	 * Initializes the Parser with the passed Token array.
	 * @param _tokens Array of Tokens
	 */
	public static void init(ArrayList<Token> _tokens) {
		tokens = _tokens;
		curTok = null;
		pos = -1;
		
		advance();
	}
	
	/**
	 * Advances to the next Token in the Token array.
	 * @return Current Token
	 */
	private static Token advance() {
		pos++;
		
		if(pos < tokens.size()) {
			curTok = tokens.get(pos);
		} else {
			curTok = null;
		}
		
		return curTok;
	}
	
	/**
	 * Creates and returns an abstract syntax tree of nodes.
	 * @return Result of parsing
	 */
	public static ParseResult parse() {
		ParseResult res = expr();
		
		if(!curTok.isType(TokenType.EOF) && res.getError() == null) {
			return res.failure(new InvalidSyntaxError(
					"Exited while parsing.",
					curTok.getStart(), curTok.getEnd()
			));
		}
		
		return res;
	}
	
	/**
	 * Creates a BinOpNode using this passed left function, right function, and TokenType array.
	 * @param lFunc		Function used to determine the left value
	 * @param rFunc		Function used to determine the right value
	 * @param types		Array of TokenTypes acceptable for the BinOpNode
	 * @return Created Node
	 */
	private static ParseResult binOp(ParseFunction lFunc, ParseFunction rFunc, TokenType[] types) {
		ParseResult res = new ParseResult();
		Node left = res.register(lFunc.run());
		if(res.getError() != null) {return res;}
		
		while(curTok != null && curTok.isType(types)) {
			Token op = curTok;
			res.register(advance());
			
			Node right = res.register(rFunc.run());
			if(res.getError() != null) {return res;}
			
			left = new BinOpNode(left, op, right);
		}
		
		return res.success(left);
	}
	
	/**
	 * Creates a BinOpNode using this passed left function, right function, and Object array.
	 * @param lFunc		Function used to determine the left value
	 * @param rFunc		Function used to determine the right value
	 * @param types		Array of Token values
	 * @return Created Node
	 */
	private static ParseResult binOp(ParseFunction lFunc, ParseFunction rFunc, Object[] values) {
		ParseResult res = new ParseResult();
		Node left = res.register(lFunc.run());
		if(res.getError() != null) {return res;}
		
		while(curTok != null && curTok.matches(TokenType.KEY, values)) {
			Token op = curTok;
			res.register(advance());
			
			Node right = res.register(rFunc.run());
			if(res.getError() != null) {return res;}
			
			left = new BinOpNode(left, op, right);
		}
		
		return res.success(left);
	}
	
	/**
	 * Returns an expression node composed of two terms added or subtracted.
	 */
	private static ParseResult expr() {
		ParseResult res = new ParseResult();
		
		if(curTok.matches(TokenType.KEY, new String[] {"var"})) {
			Node varNode = res.register(varExpr());
			if(res.getError() != null) {return res;}
			
			return res.success(varNode);
		} else if(curTok.matches(TokenType.KEY, new String[] {"if"})) {
			Node ifNode = res.register(ifExpr());
			if(res.getError() != null) {return res;}
			
			return res.success(ifNode);
		} else if(curTok.matches(TokenType.KEY, new String[] {"while"})) {
			Node whileNode = res.register(whileExpr());
			if(res.getError() != null) {return res;}
			
			return res.success(whileNode);
		} else if(curTok.matches(TokenType.KEY, new String[] {"for"})) {
			Node forNode = res.register(forExpr());
			if(res.getError() != null) {return res;}
			
			return res.success(forNode);
		} else if(curTok.isType(TokenType.LSQUARE)) {
			Node listNode = res.register(listExpr());
			if(res.getError() != null) {return res;}
			
			return res.success(listNode);
		}
		
		ParseFunction eFunc = Parser::compExpr;
		return binOp(eFunc, eFunc, new String[] {"and", "or"});
	}
	
	private static ParseResult listExpr() {
		ParseResult res = new ParseResult();
		Position start = curTok.getStart();
		ArrayList<Node> values = new ArrayList<Node>();
		
		if(!curTok.isType(TokenType.LSQUARE)) {
			return res.failure(new InvalidSyntaxError(
				"Expected '['.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		if(!curTok.isType(TokenType.RSQUARE)) {
			Node val = res.register(expr());
			if(res.getError() != null) {return res;}
			
			values.add(val);
			
			while(curTok.isType(TokenType.COMMA)) {
				res.register(advance());
				
				val = res.register(expr());
				if(res.getError() != null) {return res;}
				
				values.add(val);
			}
			
			if(!curTok.isType(TokenType.RSQUARE)) {
				return res.failure(new InvalidSyntaxError(
					"Expected ']'.",
					curTok.getStart(), curTok.getEnd()
				));
			}
		}
		
		Position end = curTok.getEnd();
		
		res.register(advance());
		
		if(values.size() > 0) {
			return res.success(new ListNode(values));
		} else {
			return res.success(new ListNode(start, end));
		}
	}
	
	private static ParseResult varExpr() {
		ParseResult res = new ParseResult();
		
		if(!curTok.matches(TokenType.KEY, new String[] {"var"})) {
			return res.failure(new InvalidSyntaxError(
				"Expected 'var'.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		if(!curTok.isType(TokenType.ID)) {
			return res.failure(new InvalidSyntaxError(
				"Expected an identifier.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		Token varName = curTok;
		
		res.register(advance());
		
		if(!curTok.isType(TokenType.EQ)) {
			return res.failure(new InvalidSyntaxError(
				"Expected '='.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		Node varVal = res.register(expr());
		if(res.getError() != null) {return res;}
		
		return res.success(new VarAssignNode(varName, varVal));
	}
	
	private static ParseResult forExpr() {
		ParseResult res = new ParseResult();
		Node iterator, condition, step = null, expression;
		
		if(!curTok.matches(TokenType.KEY, new String[] {"for"})) {
			return res.failure(new InvalidSyntaxError(
				"Expected 'for'.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		if(!curTok.isType(TokenType.LPAREN)) {
			return res.failure(new InvalidSyntaxError(
					"Expected '('.",
					curTok.getStart(), curTok.getEnd()
				));
		}
		
		res.register(advance());
		
		iterator = res.register(varExpr());
		if(res.getError() != null) {return res;}
		
		if(!curTok.isType(TokenType.COMMA)) {
			return res.failure(new InvalidSyntaxError(
				"Expected ','.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		condition = res.register(expr());
		if(res.getError() != null) {return res;}
		
		if(curTok.isType(TokenType.COMMA)) {
			res.register(advance());
			
			step = res.register(expr());
			if(res.getError() != null) {return res;}
		}
		
		if(!curTok.isType(TokenType.RPAREN)) {
			return res.failure(new InvalidSyntaxError(
				"Expected ')'.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		if(!curTok.isType(TokenType.COLON)) {
			return res.failure(new InvalidSyntaxError(
				"Expected ':'.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		expression = res.register(expr());
		if(res.getError() != null) {return res;}
		
		return res.success(new ForNode(iterator, condition, step, expression));
	}
	
	private static ParseResult ifExpr() {
		ParseResult res = new ParseResult();
		ArrayList<Node> conds = new ArrayList<Node>();
		ArrayList<Node> exprs = new ArrayList<Node>();
		Node elseCase = null;
		
		if(!curTok.matches(TokenType.KEY, new String[] {"if"})) {
			return res.failure(new InvalidSyntaxError(
				"Expected 'if'.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		if(!curTok.isType(TokenType.LPAREN)) {
			return res.failure(new InvalidSyntaxError(
				"Expected '('.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		Node condition = res.register(expr());
		if(res.getError() != null) {return res;}
		conds.add(condition);
		
		if(!curTok.isType(TokenType.RPAREN)) {
			return res.failure(new InvalidSyntaxError(
				"Expected ')'.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		if(!curTok.isType(TokenType.COLON)) {
			return res.failure(new InvalidSyntaxError(
				"Expected ':'.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		Node expression = res.register(expr());
		if(res.getError() != null) {return res;}
		exprs.add(expression);
		
		while(curTok.matches(TokenType.KEY, new String[] {"elif"})) {
			res.register(advance());
			
			if(!curTok.isType(TokenType.LPAREN)) {
				return res.failure(new InvalidSyntaxError(
					"Expected '('.",
					curTok.getStart(), curTok.getEnd()
				));
			}
			
			res.register(advance());
			
			condition = res.register(expr());
			if(res.getError() != null) {return res;}
			conds.add(condition);
			
			if(!curTok.isType(TokenType.RPAREN)) {
				return res.failure(new InvalidSyntaxError(
					"Expected ')'.",
					curTok.getStart(), curTok.getEnd()
				));
			}
			
			res.register(advance());
			
			if(!curTok.isType(TokenType.COLON)) {
				return res.failure(new InvalidSyntaxError(
					"Expected ':'.",
					curTok.getStart(), curTok.getEnd()
				));
			}
			
			res.register(advance());
			
			expression = res.register(expr());
			if(res.getError() != null) {return res;}
			exprs.add(expression);
		}
		
		if(curTok.matches(TokenType.KEY, new String[] {"else"})) {
			res.register(advance());
			
			if(!curTok.isType(TokenType.COLON)) {
				return res.failure(new InvalidSyntaxError(
					"Expected ':'.",
					curTok.getStart(), curTok.getEnd()
				));
			}
			
			res.register(advance());
			
			elseCase = res.register(expr());
			if(res.getError() != null) {return res;}
		}
		
		return res.success(new IfNode(conds, exprs, elseCase));
	}
	
	private static ParseResult whileExpr() {
		ParseResult res = new ParseResult();
		Node condition, expression;
		
		if(!curTok.matches(TokenType.KEY, new String[] {"while"})) {
			return res.failure(new InvalidSyntaxError(
				"Expected 'while'.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		if(!curTok.isType(TokenType.LPAREN)) {
			return res.failure(new InvalidSyntaxError(
				"Expected '('.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		condition = res.register(expr());
		if(res.getError() != null) {return res;}
		
		if(!curTok.isType(TokenType.RPAREN)) {
			return res.failure(new InvalidSyntaxError(
				"Expected ')'.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		if(!curTok.isType(TokenType.COLON)) {
			return res.failure(new InvalidSyntaxError(
				"Expected ':'.",
				curTok.getStart(), curTok.getEnd()
			));
		}
		
		res.register(advance());
		
		expression = res.register(expr());
		if(res.getError() != null) {return res;}
		
		return res.success(new WhileNode(condition, expression));
	}
	
	private static ParseResult compExpr() {
		ParseFunction cFunc = Parser::arithExpr;
		return binOp(cFunc, cFunc, new TokenType[] {
			TokenType.EE, TokenType.NE,
			TokenType.LT, TokenType.GT,
			TokenType.LTE, TokenType.GTE
		});
	}
	
	private static ParseResult arithExpr() {
		ParseFunction aFunc = Parser::term;
		return binOp(aFunc, aFunc, new TokenType[] {TokenType.ADD, TokenType.SUB});
	}
	
	/**
	 * Returns a term node composed of two factors multiplied or divided.
	 */
	private static ParseResult term() {
		ParseFunction tFunc = Parser::factor;
		return binOp(tFunc, tFunc, new TokenType[] {TokenType.MUL, TokenType.DIV});
	}
	
	/**
	 * Returns a single value node.
	 */
	private static ParseResult factor() {
		ParseResult res = new ParseResult();
		Token cur = curTok;
		
		if(cur.isType(TokenType.SUB, TokenType.NOT)) {
			res.register(advance());
			
			Node value = res.register(expr());
			if(res.getError() != null) {return res;}
			
			return res.success(new UnaryOpNode(cur, value));
		} else if(cur.isType(TokenType.INT, TokenType.FLOAT)) {
			res.register(advance());
			return res.success(new NumberNode(cur));
		} else if(cur.isType(TokenType.LPAREN)) {
			res.register(advance());
			
			Node value = res.register(expr());
			if(res.getError() != null) {return res;}
			
			if(!curTok.isType(TokenType.RPAREN)) {
				return res.failure(new InvalidSyntaxError("Expected ')'.", curTok.getStart(), curTok.getEnd()));
			}
			
			res.register(advance());
			return res.success(value);
		} else if(cur.isType(TokenType.ID)) {
			res.register(advance());
			return res.success(new VarAccessNode(cur));
		} else if(cur.isType(TokenType.STR)) {
			res.register(advance());
			return res.success(new StringNode(cur));
		}
		
		return res.failure(new InvalidSyntaxError("Illegal start of expression.", cur.getStart(), cur.getEnd()));
	}
}