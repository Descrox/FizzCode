package com.fizz.interpreter.value;

import java.util.Map;
import java.util.HashMap;

import com.fizz.lexer.token.Type;
import com.fizz.util.result.RuntimeResult;
import com.fizz.util.function.VFunc;
import com.fizz.util.error.RuntimeError;

/**
 * Represents a String value.
 * @author Noah James Rathman
 */
public class Str extends Val {
  private static Map<Type, VFunc> FUNCTIONS;

  /**
   * Places all operation functions into a Map for calling
   * in the Interpreter.
   */
  public static void init() {
    FUNCTIONS = new HashMap<Type, VFunc>();

    FUNCTIONS.put(Type.ADD, Str::add);
    FUNCTIONS.put(Type.GT,  Str::strAt);
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

  private static RuntimeResult add(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Str sSelf = (Str) self;

    Str result = new Str(sSelf.getValue() + other.toString());
    return res.success(result);
  }

  private static RuntimeResult strAt(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Str sSelf = (Str) self;

    if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number", other));
    }

    int idx = (int) ((Num) other).getValue();
    String sStr = sSelf.getValue();

    if(idx < 0 || idx >= sStr.length()) {
      return res.failure(
        RuntimeError.indexOutOfBounds(idx, sStr.length(), other)
      );
    }

    Str result = new Str(String.valueOf(sStr.charAt(idx)));
    return res.success(result);
  }

  //Class
  private String value;

  /**
   * Creates a new Str with the passed String value.
   * @param value Value of Str
   */
  public Str(String value) {
    super("String");
    this.value = value;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Val> T castTo(String type) {
    if(type.equals("String")) {
      return (T) this;
    } else if(type.equals("Boolean")) {
      try {
        return (T) new Bool(Boolean.parseBoolean(value));
      } catch(Exception e) {return null;}
    } else if(type.equals("Number")) {
      try {
        return (T) new Num(Double.parseDouble(value));
      } catch(Exception e) {return null;}
    }

    return null;
  }

  /**
   * Returns the value of this Str as a String.
   * @return Value of Str
   */
  public String getValue() {
    return value;
  }

  public boolean equals(Object obj) {
    if(obj instanceof Str) {
      Str oStr = (Str) obj;

      return value.equals(oStr.getValue());
    }

    return false;
  }

  public String toString() {
    return value;
  }
}
