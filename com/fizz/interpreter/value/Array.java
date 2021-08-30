package com.fizz.interpreter.value;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.fizz.lexer.token.Type;
import com.fizz.util.result.RuntimeResult;
import com.fizz.util.function.VFunc;
import com.fizz.util.error.RuntimeError;

/**
 * Holds a list of values.
 * @author Noah James Rathman
 */
public class Array extends Val {
  private static Map<Type, VFunc> FUNCTIONS;

  /**
   * Places all operation functions into a Map for calling
   * in the Interpreter.
   */
  public static void init() {
    FUNCTIONS = new HashMap<Type, VFunc>();

    FUNCTIONS.put(Type.EE,  Val::equalTo);
    FUNCTIONS.put(Type.NE,  Val::notEqualTo);
    FUNCTIONS.put(Type.LT,  Array::add);
    FUNCTIONS.put(Type.SUB, Array::remove);
    FUNCTIONS.put(Type.GT,  Array::get);
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
    Array aSelf = (Array) self;

    if(aSelf.getValueType() == null) {
      aSelf.setValueType(other.getType());
    } else if(!other.isType(aSelf.getValueType())) {
      return res.failure(
        RuntimeError.unexpectedType(aSelf.getValueType(), other)
      );
    }

    aSelf.getValues().add(other);
    return res.success(aSelf);
  }

  private static RuntimeResult remove(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    Array aSelf = (Array) self;

    if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number", other));
    }

    int idx = (int) ((Num) other).getValue();
    if(idx < 0 || idx >= aSelf.getValues().size()) {
      return res.failure(
        RuntimeError.indexOutOfBounds(idx, aSelf.getValues().size(), other)
      );
    }

    aSelf.getValues().remove(idx);
    return res.success(aSelf);
  }

  private static RuntimeResult get(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    Array aSelf = (Array) self;

    if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number", other));
    }

    int idx = (int) ((Num) other).getValue();
    if(idx < 0 || idx >= aSelf.getValues().size()) {
      return res.failure(
        RuntimeError.indexOutOfBounds(idx, aSelf.getValues().size(), other)
      );
    }

    Val got = aSelf.getValues().get(idx);
    return res.success(got);
  }

  //Class
  private String valType;
  private List<Val> values;

  /**
   * Creates a new Array with the passed value type and values.
   * @param valType Type of values that can be in this Array
   * @param values Already existing Array values
   */
  public Array(String valType, List<Val> values) {
    super("Array");
    this.valType = valType;
    this.values = values;
  }

  /**
   * Returns a List of the values held in this Array.
   * @return Array values
   */
  public List<Val> getValues() {
    return values;
  }

  /**
   * Returns the value type of this Array.
   * @return Array value type
   */
  public String getValueType() {
    return valType;
  }

  /**
   * Sets the value type of this array to the passed type.
   * @param valType New Array value type
   */
  public void setValueType(String valType) {
    this.valType = valType;
  }

  public boolean equals(Object obj) {
    if(obj instanceof Array) {
      Array arr = (Array) obj;
      return values.equals(arr.getValues());
    }

    return false;
  }

  public String toString() {
    return values.toString();
  }
}
