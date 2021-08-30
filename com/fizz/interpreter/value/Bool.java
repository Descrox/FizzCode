package com.fizz.interpreter.value;

import java.util.Map;
import java.util.HashMap;

import com.fizz.lexer.token.Type;
import com.fizz.util.result.RuntimeResult;
import com.fizz.util.function.VFunc;
import com.fizz.util.error.RuntimeError;

/**
 * Represents a boolean value.
 * @author Noah James Rathman
 */
public class Bool extends Val {
  private static Map<Type, VFunc> FUNCTIONS;

  /**
   * Places all operation functions into a Map for calling
   * in the Interpreter.
   */
  public static void init() {
    FUNCTIONS = new HashMap<Type, VFunc>();

    FUNCTIONS.put(Type.AND, Bool::and);
    FUNCTIONS.put(Type.OR,  Bool::or);
    FUNCTIONS.put(Type.EE,  Val::equalTo);
    FUNCTIONS.put(Type.NE,  Val::notEqualTo);
  }

  /**
   * Returns the function associated with the passed operation
   * type.
   * @param opType Type of operation
   * @return Function associated with the passed operation Type.
   * If no function is associated, this method returns null.
   */
  public static VFunc getFunction(Type opType) {
    return FUNCTIONS.get(opType);
  }

  private static RuntimeResult and(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Bool bSelf = (Bool) self;

    if(!other.isType("Boolean")) {
      return res.failure(RuntimeError.unexpectedType("Boolean", other));
    }

    final Bool oBool = (Bool) other;
    Bool result = new Bool(bSelf.getValue() && oBool.getValue());

    return res.success(result);
  }

  private static RuntimeResult or(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Bool bSelf = (Bool) self;

    if(!other.isType("Boolean")) {
      return res.failure(RuntimeError.unexpectedType("Boolean", other));
    }

    final Bool oBool = (Bool) other;
    Bool result = new Bool(bSelf.getValue() || oBool.getValue());

    return res.success(result);
  }

  //Class
  private boolean value;

  /**
   * Creates a new Bool with the passed boolean value.
   * @param value Value of Bool
   */
  public Bool(boolean value) {
    super("Boolean");
    this.value = value;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Val> T castTo(String type) {
    if(type.equals("Boolean")) {
      return (T) this;
    } else if(type.equals("Number")) {
      return (T) new Num(value ? 1 : 0);
    } else if(type.equals("String")) {
      return (T) new Str(String.valueOf(value));
    }

    return null;
  }

  /**
   * Returns the value of this Bool as a boolean.
   * @return Value of Bool
   */
  public boolean getValue() {
    return value;
  }

  /**
   * Returns the inverse value of this Bool.
   * @return New Bool with the opposite value of this one
   */
  public Bool not() {
    return new Bool(!value);
  }

  public boolean equals(Object obj) {
    if(obj instanceof Bool) {
      Bool oBool = (Bool) obj;

      return value == oBool.getValue();
    }

    return false;
  }

  public String toString() {
    return String.valueOf(value);
  }
}
