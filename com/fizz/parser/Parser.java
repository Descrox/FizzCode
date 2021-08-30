package com.fizz.parser;

import java.util.List;
import java.util.ArrayList;

import com.fizz.lexer.token.*;
import com.fizz.parser.node.*;
import com.fizz.util.Position;
import com.fizz.util.error.InvalidSyntaxError;
import com.fizz.util.result.ParseResult;
import com.fizz.util.function.PFunc;

/**
 * Used for the creation of Abstract Syntax Trees, which are passed to the
 * Interpreter.
 * @author Noah James Rathman
 */
public final class Parser {
  private static List<Token> tokens;
  private static Token curTok, lastTok;
  private static int pos;

  //Parser should never be instanced.
  private Parser() {}

  /**
   * Restarts the Parser and prepares it to create a new Abstract Syntax Tree.
   * @param _tokens New Token List
   */
  public static void init(List<Token> _tokens) {
    tokens = _tokens;
    curTok = null;
    pos = -1;

    advance();
  }

  private static void advance() {
    pos++;
    lastTok = curTok;
    curTok = (pos < tokens.size() ? tokens.get(pos) : null);
  }

  /**
   * Parses the List of Tokens into an Abstract Syntax Tree.
   * @return Result of parsing
   */
  public static ParseResult parse() {
    ParseResult res = statement();

    if(!res.hasError() && !curTok.isType(Type.EOF)) {
      return res.failure(new InvalidSyntaxError(
        "Parser exited due to unexpected token: \"" + curTok + "\".",
        curTok.getStart(), curTok.getEnd()
      ));
    }

    return res;
  }

  private static ParseResult factor() {
    ParseResult res = new ParseResult();
    Token cur = curTok;

    if(curTok.isType(Type.SUB, Type.NOT)) {
      advance();

      Node node = res.register(factor());
      if(res.hasError()) {return res;}

      return res.success(new UnaryOpNode(cur, node));
    } else if(curTok.isType(Type.INT, Type.FLOAT, Type.STR)) {
      advance();
      return res.success(new ValueNode(cur));
    } else if(curTok.isType(Type.LPAREN)) {
      advance();

      Node node = res.register(expr());
      if(res.hasError()) {return res;}

      if(!curTok.isType(Type.RPAREN)) {
        return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.RPAREN));
      }

      advance();
      return res.success(node);
    } else if(curTok.isType(Type.ID)) {
      advance();

      if(curTok.isType(Type.LPAREN)) {
        List<Node> argValues = new ArrayList<Node>();
        advance();

        while(!curTok.isType(Type.RPAREN)) {
          Node argVal = res.register(expr());
          if(res.hasError()) {return res;}

          argValues.add(argVal);
          if(curTok.isType(Type.COMMA)) {advance();}
          else if(!curTok.isType(Type.RPAREN)) {
            return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.COMMA, Type.RPAREN));
          }
        }

        advance();
        return res.success(new CallNode(cur, argValues));
      } else if(curTok.isType(Type.EQ)) {
        advance();

        Node value = res.register(expr());
        if(res.hasError()) {return res;}

        return res.success(new VarAssignNode(cur, value, false, (byte) 1));
      }

      return res.success(new VarAccessNode(cur));
    } else if(curTok.isType(Type.LSQUARE)) {
      Position start = curTok.getStart();
      List<Node> values = new ArrayList<Node>();

      advance();
      Node node = null;
      while(!curTok.isType(Type.RSQUARE)) {
        node = res.register(expr());
        if(res.hasError()) {return res;}

        values.add(node);
        if(curTok.isType(Type.COMMA)) {
          advance();
        } else if(!curTok.isType(Type.RSQUARE)) {
          return res.failure(
            InvalidSyntaxError.expectedTokenType(curTok, Type.COMMA, Type.RSQUARE)
          );
        }
      }

      Position end = curTok.getEnd();
      advance();

      Node result = values.size() > 0 ? new ArrayNode(values) : new ArrayNode(start, end);
      return res.success(result);
    } else if(curTok.isType(Type.KEY) && curTok.hasValue("cast")) {
      advance();

      if(!curTok.isType(Type.ID)) {
        return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.ID));
      }

      Token cast = curTok;
      advance();

      Node expr = res.register(expr());
      if(res.hasError()) {return res;}

      return res.success(new CastNode(cast, expr));
    }

    Type[] expt = {Type.INT, Type.FLOAT, Type.SUB, Type.LPAREN, Type.LSQUARE, Type.ID};
    return res.failure(InvalidSyntaxError.expectedTokenType(curTok, expt));
  }

  private static ParseResult term() {
    PFunc func = Parser::factor;
    Type[] ops = {Type.MUL, Type.DIV};

    return binOp(func, func, ops);
  }

  private static ParseResult arith() {
    PFunc func = Parser::term;
    Type[] ops = {Type.ADD, Type.SUB};

    return binOp(func, func, ops);
  }

  private static ParseResult comp() {
    PFunc func = Parser::arith;
    Type[] ops = {
      Type.EE, Type.NE,
      Type.LT, Type.GT,
      Type.LTE, Type.GTE
    };

    return binOp(func, func, ops);
  }

  private static ParseResult expr() {
    PFunc func = Parser::comp;
    Type[] ops = {Type.AND, Type.OR};

    return binOp(func, func, ops);
  }

  private static ParseResult varCmd() {
    ParseResult res = new ParseResult();
    Token name = null;
    Node expr = null;
    boolean constant = curTok.hasValue("const");

    advance();
    if(!curTok.isType(Type.ID)) {
      return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.ID));
    }

    name = curTok;
    advance();

    if(!curTok.isType(Type.EQ)) {
      return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.EQ));
    }

    advance();
    expr = res.register(expr());
    if(res.hasError()) {return res;}

    return res.success(new VarAssignNode(name, expr, constant, (byte) 0));
  }

  private static ParseResult funcCmd() {
    ParseResult res = new ParseResult();
    Token name = null;
    List<String> argNames = new ArrayList<String>();
    Node statement = null;

    advance();
    if(!curTok.isType(Type.ID)) {
      return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.ID));
    }

    name = curTok;
    advance();
    if(!curTok.isType(Type.LPAREN)) {
      return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.LPAREN));
    }

    advance();
    String aName;
    while(curTok.isType(Type.ID)) {
      aName = curTok.getValue();
      argNames.add(aName);

      advance();
      if(curTok.isType(Type.COMMA)) {
        advance();

        if(!curTok.isType(Type.ID)) {
          return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.ID));
        }
      } else if(!curTok.isType(Type.RPAREN)) {
        return res.failure(
          InvalidSyntaxError.expectedTokenType(curTok, Type.COMMA, Type.RPAREN)
        );
      }
    }

    advance();
    if(!curTok.isType(Type.LBRACE)) {
      return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.LBRACE));
    }

    advance();
    statement = res.register(statement());
    if(res.hasError()) {return res;}

    if(!curTok.isType(Type.RBRACE)) {
      return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.RBRACE));
    }

    advance();
    return res.success(new FuncDefNode(name, argNames, statement));
  }

  private static ParseResult ifCmd() {
    ParseResult res = new ParseResult();
    List<Node> conds = new ArrayList<Node>();
    List<Node> exprs = new ArrayList<Node>();
    Node elseCase = null;

    do {
      advance();
      if(!curTok.isType(Type.LPAREN)) {
        return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.LPAREN));
      }

      advance();
      Node cond = res.register(expr());
      if(res.hasError()) {return res;}

      if(!curTok.isType(Type.RPAREN)) {
        return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.RPAREN));
      }

      advance();
      if(!curTok.isType(Type.LBRACE)) {
        return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.LBRACE));
      }

      advance();
      Node expr = res.register(statement());
      if(res.hasError()) {return res;}

      if(!curTok.isType(Type.RBRACE)) {
        return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.RBRACE));
      }

      advance();
      conds.add(cond);
      exprs.add(expr);
    } while(curTok.isType(Type.KEY) && curTok.hasValue("elif"));

    if(curTok.isType(Type.KEY) && curTok.hasValue("else")) {
      advance();

      if(!curTok.isType(Type.LBRACE)) {
        return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.LBRACE));
      }

      advance();
      elseCase = res.register(statement());
      if(res.hasError()) {return res;}

      if(!curTok.isType(Type.RBRACE)) {
        return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.RBRACE));
      }

      advance();
    }

    return res.success(new IfNode(conds, exprs, elseCase));
  }

  private static ParseResult whileCmd() {
    ParseResult res = new ParseResult();
    advance();

    if(!curTok.isType(Type.LPAREN)) {
      return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.LPAREN));
    }

    advance();
    Node cond = res.register(expr());
    if(res.hasError()) {return res;}

    if(!curTok.isType(Type.RPAREN)) {
      return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.RPAREN));
    }

    advance();
    if(!curTok.isType(Type.LBRACE)) {
      return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.LBRACE));
    }

    advance();
    Node expr = res.register(statement());
    if(res.hasError()) {return res;}

    if(!curTok.isType(Type.RBRACE)) {
      return res.failure(InvalidSyntaxError.expectedTokenType(curTok, Type.RBRACE));
    }

    advance();
    return res.success(new WhileNode(cond, expr));
  }

  private static ParseResult forCmd() {
    ParseResult res = new ParseResult();
    Node iter, cond, step = null, expr;

    advance();
    if(!curTok.isType(Type.LPAREN)) {
      return res.failure(
        InvalidSyntaxError.expectedTokenType(curTok, Type.LPAREN)
      );
    }

    advance();
    if(!curTok.hasValue("var")) {
      return res.failure(
        InvalidSyntaxError.expectedToken(curTok, new Token(Type.KEY, "var"))
      );
    }

    iter = res.register(varCmd());
    if(res.hasError()) {return res;}

    if(!curTok.isType(Type.EOL)) {
      return res.failure(
        InvalidSyntaxError.expectedTokenType(curTok, Type.EOL)
      );
    }

    advance();
    cond = res.register(expr());
    if(res.hasError()) {return res;}

    if(curTok.isType(Type.EOL)) {
      advance();

      step = res.register(expr());
      if(res.hasError()) {return res;}
    }

    if(!curTok.isType(Type.RPAREN)) {
      return res.failure(
        InvalidSyntaxError.expectedTokenType(curTok, Type.RPAREN)
      );
    }

    advance();
    if(!curTok.isType(Type.LBRACE)) {
      return res.failure(
        InvalidSyntaxError.expectedTokenType(curTok, Type.LBRACE)
      );
    }

    advance();
    expr = res.register(statement());
    if(res.hasError()) {return res;}

    if(!curTok.isType(Type.RBRACE)) {
      return res.failure(
        InvalidSyntaxError.expectedTokenType(curTok, Type.RBRACE)
      );
    }

    advance();
    return res.success(new ForNode(iter, cond, step, expr));
  }

  private static ParseResult command() {
    ParseResult res = new ParseResult();

    if(curTok.isType(Type.KEY)) {
      Node result = null;

      if(curTok.hasValue("var", "const")) {
        result = res.register(varCmd());
      } else if(curTok.hasValue("return")) {
        advance();
        result = res.register(expr());
        if(!res.hasError()) {
          result = new ReturnNode(result);
        }
      } else if(curTok.hasValue("func")) {
        result = res.register(funcCmd());
      } else if(curTok.hasValue("if")) {
        result = res.register(ifCmd());
      } else if(curTok.hasValue("while")) {
        result = res.register(whileCmd());
      } else if(curTok.hasValue("break", "continue")) {
        Token cur = curTok;
        advance();

        result = new ControlNode(cur.getValue(), cur.getStart(), cur.getEnd());
      } else if(curTok.hasValue("for")) {
        result = res.register(forCmd());
      }

      if(res.hasError()) {return res;}
      return res.success(result);
    }

    return expr();
  }

  private static ParseResult statement() {
    ParseResult res = new ParseResult();
    List<Node> statements = new ArrayList<Node>();
    Node node;

    do {
      node = res.register(command());
      if(res.hasError()) {return res;}

      statements.add(node);
      if(lastTok.isType(Type.RBRACE)) {continue;}
      if(!curTok.isType(Type.EOL)) {
        return res.failure(
          InvalidSyntaxError.expectedTokenType(curTok, Type.EOL)
        );
      }

      advance();
    } while(!curTok.isType(Type.RBRACE, Type.EOF));

    return res.success(new StatementNode(statements));
  }

  private static ParseResult binOp(PFunc lFunc, PFunc rFunc, Type[] ops) {
    ParseResult res = new ParseResult();

    Node left = res.register(lFunc.run());
    if(res.hasError()) {return res;}

    Token op = null;
    Node right = null;
    while(curTok.isType(ops)) {
      op = curTok;
      advance();

      right = res.register(rFunc.run());
      if(res.hasError()) {return res;}

      left = new BinOpNode(left, op, right);
    }

    return res.success(left);
  }
}
