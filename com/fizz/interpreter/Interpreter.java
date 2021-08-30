package com.fizz.interpreter;

import static com.fizz.util.error.RuntimeError.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.fizz.lexer.token.*;
import com.fizz.parser.node.*;
import com.fizz.interpreter.value.*;
import com.fizz.util.Constants;
import com.fizz.util.context.*;
import com.fizz.util.result.RuntimeResult;
import com.fizz.util.function.IFunc;
import com.fizz.util.function.VFunc;
import com.fizz.util.error.RuntimeError;

/**
 * Interprets the Abstract Syntax Tree and returns the resulting
 * value created by it.
 * @author Noah James Rathman
 */
public class Interpreter {
  private static Map<String, IFunc> FUNCTIONS;
  private static boolean inLoop = false;

  //Interpreter should never be instanced.
  private Interpreter() {}

  /**
   * Initializes the Interpreter by putting all visit methods
   * into a Map.
   */
  public static void init() {
    FUNCTIONS = new HashMap<String, IFunc>();

    FUNCTIONS.put("ValueNode",          Interpreter::visitValueNode);
    FUNCTIONS.put("UnaryOpNode",        Interpreter::visitUnaryOpNode);
    FUNCTIONS.put("BinOpNode",          Interpreter::visitBinOpNode);
    FUNCTIONS.put("VarAccessNode",      Interpreter::visitVarAccessNode);
    FUNCTIONS.put("VarAssignNode",      Interpreter::visitVarAssignNode);
    FUNCTIONS.put("CallNode",           Interpreter::visitCallNode);
    FUNCTIONS.put("FuncDefNode",        Interpreter::visitFuncDefNode);
    FUNCTIONS.put("StatementNode",      Interpreter::visitStatementNode);
    FUNCTIONS.put("ReturnNode",         Interpreter::visitReturnNode);
    FUNCTIONS.put("ArrayNode",          Interpreter::visitArrayNode);
    FUNCTIONS.put("CastNode",           Interpreter::visitCastNode);
    FUNCTIONS.put("IfNode",             Interpreter::visitIfNode);
    FUNCTIONS.put("WhileNode",          Interpreter::visitWhileNode);
    FUNCTIONS.put("ControlNode",        Interpreter::visitControlNode);
    FUNCTIONS.put("ForNode",            Interpreter::visitForNode);
  }

  /**
   * Visits the passed Node using the passed Context.
   * @param node Node to be visited
   * @param context Context used while visiting Node
   * @return Result of visitation
   */
  public static RuntimeResult visit(Node node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final String className = node.getClass().getSimpleName();

    IFunc func = FUNCTIONS.get(className);
    if(func != null) {
      return func.run(node, context);
    }

    return res.failure(new RuntimeError(
      "Node Visit",
      "No visit method defined for \"" + className + "\".",
      node.getStart(), node.getEnd(), context
    ));
  }

  private static RuntimeResult visitValueNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final ValueNode node = (ValueNode) _node;

    Val result = null;
    if(node.getValue().isType(Type.INT, Type.FLOAT)) {
      result = new Num(Double.parseDouble(node.getValue().getValue()));
    } else if(node.getValue().isType(Type.STR)) {
      result = new Str(node.getValue().getValue());
    }

    result.setPosition(node.getStart(), node.getEnd());
    result.setContext(context);

    return res.success(result);
  }

  private static RuntimeResult visitUnaryOpNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final UnaryOpNode node = (UnaryOpNode) _node;

    Val value = res.register(visit(node.getNode(), context));
    if(res.hasError()) {return res;}

    Val result = null;
    if(value.isType("Number")) {
      if(!node.getOp().isType(Type.SUB)) {
        return res.failure(unexpectedOperation("Number", node.getOp(), context));
      }

      VFunc func = Num.getFunction(Type.MUL);
      result = res.register(func.run(value, new Num(-1)));
      if(res.hasError()) {return res;}
    } else if(value.isType("Boolean")) {
      Bool bVal = (Bool) value;
      result = bVal.not();
    } else {
      return res.failure(illegalOperation("unary", value));
    }

    result.setPosition(node.getStart(), node.getEnd());
    result.setContext(context);

    return res.success(result);
  }

  private static RuntimeResult visitBinOpNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final BinOpNode node = (BinOpNode) _node;

    Val lVal = res.register(visit(node.getLeftNode(), context));
    if(res.hasError()) {return res;}

    Val rVal = res.register(visit(node.getRightNode(), context));
    if(res.hasError()) {return res;}

    Val result = null;
    if(lVal.isType("Number")) {
      VFunc func = Num.getFunction(node.getOp().getType());
      if(func == null) {
        return res.failure(unexpectedOperation("Number", node.getOp(), context));
      }

      result = res.register(func.run(lVal, rVal));
      if(res.hasError()) {return res;}
    } else if(lVal.isType("String")) {
      VFunc func = Str.getFunction(node.getOp().getType());
      if(func == null) {
        return res.failure(unexpectedOperation("String", node.getOp(), context));
      }

      result = res.register(func.run(lVal, rVal));
      if(res.hasError()) {return res;}
    } else if(lVal.isType("Boolean")) {
      VFunc func = Bool.getFunction(node.getOp().getType());
      if(func == null) {
        return res.failure(unexpectedOperation("Boolean", node.getOp(), context));
      }

      result = res.register(func.run(lVal, rVal));
    } else if(lVal.isType("Array")) {
      VFunc func = Array.getFunction(node.getOp().getType());
      if(func == null) {
        return res.failure(unexpectedOperation("Array", node.getOp(), context));
      }

      result = res.register(func.run(lVal, rVal));
    } else {
      return res.failure(illegalOperation("binary", lVal));
    }

    if(res.hasError()) {return res;}

    result.setPosition(node.getStart(), node.getEnd());
    result.setContext(context);

    return res.success(result);
  }

  private static RuntimeResult visitVarAccessNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final VarAccessNode node = (VarAccessNode) _node;

    final String ID = node.getName().getValue();
    Symbol sym = context.getSymbolTable().get(ID);
    if(sym == null) {
      return res.failure(undefinedSymbol(ID, node.getName(), context));
    }

    Val symVal = sym.getValue();
    symVal.setPosition(node.getStart(), node.getEnd());
    symVal.setContext(context);

    return res.success(symVal);
  }

  private static RuntimeResult visitVarAssignNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final VarAssignNode node = (VarAssignNode) _node;

    String symID = node.getName().getValue();
    Symbol sym = context.getSymbolTable().get(symID);
    if(node.getType() == 1 && sym == null) {
      return res.failure(
        undefinedSymbol(symID, node.getName(), context)
      );
    }

    if(sym != null && sym.isConstant()) {
      return res.failure(illegalAssign(symID, node, context));
    }

    Val symVal = res.register(visit(node.getExpr(), context));
    if(res.hasError()) {return res;}

    if(symVal.isType("void")) {
      return res.failure(voidValue(node, context));
    }

    context.getSymbolTable().set(symID, symVal, node.isConstant());
    return res.success(Val.createVoid(node.getStart(), node.getEnd(), context));
  }

  private static RuntimeResult visitCallNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final CallNode node = (CallNode) _node;

    final String ID = node.getName().getValue();
    Symbol sym = context.getSymbolTable().get(ID);
    if(sym == null) {
      return res.failure(undefinedSymbol(ID, node.getName(), context));
    }

    Val sVal = sym.getValue();
    if(!sVal.isType("Function")) {
      return res.failure(new RuntimeError(
        "Symbol Access",
        "\"" + ID + "\" is not a Function.",
        node.getStart(), node.getEnd(), context
      ));
    }

    BaseFunc func = (BaseFunc) sVal;
    func.setPosition(node.getStart(), node.getEnd());

    List<Val> argValues = new ArrayList<Val>();
    for(Node aNode : node.getArgValues()) {
      Val argValue = res.register(visit(aNode, context));
      if(res.hasError()) {return res;}

      if(argValue.isType("void")) {
        return res.failure(voidValue(aNode, context));
      }
      argValues.add(argValue);
    }

    Val result = res.register(func.execute(argValues, context));
    if(res.hasError()) {return res;}

    if(!result.getAttribute().equals("returned")) {
      result = Val.createVoid(node.getStart(), node.getEnd(), context);
    }

    result.setPosition(node.getStart(), node.getEnd());
    result.setContext(context);
    result.setAttribute("none");

    return res.success(result);
  }

  private static RuntimeResult visitFuncDefNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final FuncDefNode node = (FuncDefNode) _node;

    String id = node.getName();
    UserFunc func = new UserFunc(id, node.getArgNames(), node.getStatement());

    if(!context.getSymbolTable().set(id, func, true)) {
      return res.failure(illegalAssign(id, node, context));
    }

    return res.success(Val.createNull(node.getStart(), node.getEnd(), context));
  }

  private static RuntimeResult visitStatementNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final StatementNode node = (StatementNode) _node;

    Val result = Val.createVoid(node.getStart(), node.getEnd(), context);
    for(Node n : node.getStatements()) {
      Val value = res.register(visit(n, context));
      if(res.hasError()) {return res;}

      String attrb = value.getAttribute();
      if(!attrb.equals("none")) {
        result = value;
        break;
      }
    }

    return res.success(result);
  }

  private static RuntimeResult visitReturnNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final ReturnNode node = (ReturnNode) _node;

    if(!context.getName().startsWith("<Function ")) {
      return res.failure(new RuntimeError(
        "Illegal Return",
        "\"return\" can not be called outside of a function.",
        node.getStart(), node.getEnd(), context
      ));
    }

    Val result = res.register(visit(node.getExpression(), context));
    if(res.hasError()) {return res;}

    result.setAttribute("returned");
    return res.success(result);
  }

  private static RuntimeResult visitArrayNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final ArrayNode node = (ArrayNode) _node;

    List<Val> values = new ArrayList<Val>();
    String valType = null;
    if(node.getValues() != null) {
      for(Node nVal : node.getValues()) {
        Val value = res.register(visit(nVal, context));
        if(res.hasError()) {return res;}

        if(valType == null) {
          if(value.isType("void")) {
            return res.failure(voidValue(nVal, context));
          } else if(!value.isType("null")) {
            valType = value.getType();
          }
        } else if(!value.isType(valType)) {
          return res.failure(unexpectedType(valType, value));
        }

        values.add(value);
      }
    }

    Array arr = new Array(valType, values);
    arr.setPosition(node.getStart(), node.getEnd());
    arr.setContext(context);

    return res.success(arr);
  }

  private static RuntimeResult visitCastNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final CastNode node = (CastNode) _node;

    final String cast = node.getCast().getValue();
    if(!Val.typeExists(cast)) {
      return res.failure(new RuntimeError(
        "Casting",
        "Type \"" + cast + "\" does not exist.",
        node.getCast().getStart(), node.getCast().getEnd(), context
      ));
    }

    Val oVal = res.register(visit(node.getExpression(), context));
    if(res.hasError()) {return res;}

    Val nVal = oVal.castTo(cast);
    if(nVal == null) {
      return res.failure(new RuntimeError(
        "Casting",
        "Cast from " + oVal.getType() + " to " + cast + " failed.",
        node.getStart(), node.getEnd(), context
      ));
    }

    nVal.setPosition(node.getStart(), node.getEnd());
    nVal.setContext(context);

    return res.success(nVal);
  }

  private static RuntimeResult visitIfNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final IfNode node = (IfNode) _node;

    Val result = null;
    int idx = 0;
    for(; idx < node.getConditions().size(); idx++) {
      Node cond = node.getConditions().get(idx);
      Val cRes = res.register(visit(cond, context));
      if(res.hasError()) {return res;}

      if(!cRes.isType("Boolean")) {
        return res.failure(unexpectedType("Boolean", cRes));
      } else if(((Bool) cRes).getValue()) {
        break;
      }
    }

    if(idx == node.getConditions().size()) {
      if(node.getElseCase() != null) {
        result = res.register(visit(node.getElseCase(), context));
        if(res.hasError()) {return res;}
      } else {
        result = Val.createVoid(node.getStart(), node.getEnd(), context);
      }
    } else {
      result = res.register(visit(node.getExpressions().get(idx), context));
      if(res.hasError()) {return res;}
    }

    return res.success(result);
  }

  private static RuntimeResult visitWhileNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final WhileNode node = (WhileNode) _node;

    Val cVal = res.register(visit(node.getCondition(), context));
    if(res.hasError()) {return res;}

    if(!cVal.isType("Boolean")) {
      return res.failure(unexpectedType("Boolean", cVal));
    }

    boolean il = inLoop;
    inLoop = true;
    Val result = Val.createVoid(node.getStart(), node.getEnd(), context);

    while(((Bool) cVal).getValue()) {
      Val eVal = res.register(visit(node.getExpression(), context));
      if(res.hasError()) {return res;}

      if(eVal.getAttribute().equals("returned")) {
        result = eVal;
        break;
      } else if(eVal.getAttribute().equals("break")) {
        break;
      } else {
        cVal = res.register(visit(node.getCondition(), context));
        if(res.hasError()) {return res;}

        if(!cVal.isType("Boolean")) {
          return res.failure(unexpectedType("Boolean", cVal));
        }
      }
    }

    inLoop = il;
    return res.success(result);
  }

  private static RuntimeResult visitControlNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final ControlNode node = (ControlNode) _node;

    if(!inLoop) {
      return res.failure(new RuntimeError(
        "Illegal Control Call",
        "\"" + node.getType() + "\" can not be called outside of a loop.",
        node.getStart(), node.getEnd(), context
      ));
    }

    Val result = Val.createVoid(node.getStart(), node.getEnd(), context);
    result.setAttribute(node.getType());

    return res.success(result);
  }

  private static RuntimeResult visitForNode(Node _node, Context context) {
    RuntimeResult res = new RuntimeResult();
    final ForNode node = (ForNode) _node;

    VarAssignNode iter = (VarAssignNode) node.getIterator();
    final String iName = iter.getName().getValue();

    res.register(visit(iter, context));
    if(res.hasError()) {return res;}

    Symbol iSym = context.getSymbolTable().get(iName);
    if(!iSym.getValue().isType("Number")) {
      context.getSymbolTable().remove(iName);
      return res.failure(unexpectedType("Number", iSym.getValue()));
    }

    Val cond = res.register(visit(node.getCondition(), context));
    if(res.hasError()) {
      context.getSymbolTable().remove(iName);
      return res;
    } else if(!cond.isType("Boolean")) {
      context.getSymbolTable().remove(iName);
      return res.failure(unexpectedType("Boolean", cond));
    }

    Num step = new Num(1);
    if(node.getStep() != null) {
      Val sVal = res.register(visit(node.getStep(), context));
      if(res.hasError()) {
        context.getSymbolTable().remove(iName);
        return res;
      } else if(!sVal.isType("Number")) {
        context.getSymbolTable().remove(iName);
        return res.failure(unexpectedType("Number", sVal));
      }

      step = (Num) sVal;
    }

    Val result = Val.createVoid(node.getStart(), node.getEnd(), context);
    final VFunc func = Num.getFunction(Type.ADD);
    while(((Bool) cond).getValue()) {
      Val eVal = res.register(visit(node.getExpression(), context));
      if(res.hasError()) {
        context.getSymbolTable().remove(iName);
        return res;
      }

      if(eVal.getAttribute().equals("returned")) {
        result = eVal;
        break;
      } else if(eVal.getAttribute().equals("break")) {
        break;
      }

      Val nIVal = res.register(func.run(iSym.getValue(), step));
      if(res.hasError()) {
        context.getSymbolTable().remove(iName);
        return res;
      }

      context.getSymbolTable().set(iName, nIVal, false);
      iSym = context.getSymbolTable().get(iName);

      cond = res.register(visit(node.getCondition(), context));
      if(res.hasError()) {
        context.getSymbolTable().remove(iName);
        return res;
      }
    }

    context.getSymbolTable().remove(iName);
    return res.success(result);
  }
}
