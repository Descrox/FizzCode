package com.fizz.interpreter.value;

import java.util.Map;
import java.util.HashMap;

import com.fizz.lexer.token.Type;
import com.fizz.util.result.RuntimeResult;
import com.fizz.util.function.VFunc;
import com.fizz.util.error.RuntimeError;

/**
 * Represents a number value of either a float of an integer.
 * @author Noah James Rathman
 */
public class Num extends Val {
  private static Map<Type, VFunc> FUNCTIONS;

  /**
   * Places all operation functions into a Map for calling
   * in the Interpreter.
   */
  public static void init() {
    FUNCTIONS = new HashMap<Type, VFunc>();

    FUNCTIONS.put(Type.ADD,   Num::add);
    FUNCTIONS.put(Type.SUB,   Num::subtract);
    FUNCTIONS.put(Type.MUL,   Num::multiply);
    FUNCTIONS.put(Type.DIV,   Num::divide);
    FUNCTIONS.put(Type.LT,    Num::lessThan);
    FUNCTIONS.put(Type.GT,    Num::greaterThan);
    FUNCTIONS.put(Type.LTE,   Num::lessThanOrEqual);
    FUNCTIONS.put(Type.GTE,   Num::greaterThanOrEqual);
    FUNCTIONS.put(Type.EE,    Val::equalTo);
    FUNCTIONS.put(Type.NE,    Val::notEqualTo);
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
    final Num nSelf = (Num) self;

    if(other.isType("String")) {
      return res.success(new Str(self.toString() + other));
    } else if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number or String", other));
    }

    double newNum = nSelf.getValue() + ((Num) other).getValue();
    Num result = new Num(newNum);

    return res.success(result);
  }

  private static RuntimeResult subtract(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Num nSelf = (Num) self;

    if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number", other));
    }

    double newNum = nSelf.getValue() - ((Num) other).getValue();
    Num result = new Num(newNum);

    return res.success(result);
  }

  private static RuntimeResult multiply(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Num nSelf = (Num) self;

    if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number", other));
    }

    double newNum = nSelf.getValue() * ((Num) other).getValue();
    Num result = new Num(newNum);

    return res.success(result);
  }

  private static RuntimeResult divide(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Num nSelf = (Num) self;

    if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number", other));
    }

    Num oNum = (Num) other;
    if(oNum.getValue() == 0) {
      return res.failure(new RuntimeError(
        "Arithmetic", "Division by 0.",
        other.getStart(), other.getEnd(), other.getContext()
      ));
    }

    double newNum = nSelf.getValue() / oNum.getValue();
    Num result = new Num(newNum);

    return res.success(result);
  }

  private static RuntimeResult lessThan(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Num nSelf = (Num) self;

    if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number", other));
    }

    Bool result = new Bool(nSelf.getValue() < ((Num) other).getValue());
    return res.success(result);
  }

  private static RuntimeResult greaterThan(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Num nSelf = (Num) self;

    if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number", other));
    }

    Bool result = new Bool(nSelf.getValue() > ((Num) other).getValue());
    return res.success(result);
  }

  private static RuntimeResult lessThanOrEqual(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Num nSelf = (Num) self;

    if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number", other));
    }

    Bool result = new Bool(nSelf.getValue() <= ((Num) other).getValue());
    return res.success(result);
  }

  private static RuntimeResult greaterThanOrEqual(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    final Num nSelf = (Num) self;

    if(!other.isType("Number")) {
      return res.failure(RuntimeError.unexpectedType("Number", other));
    }

    Bool result = new Bool(nSelf.getValue() >= ((Num) other).getValue());
    return res.success(result);
  }

  //Class
  private double value;

  /**
   * Creates a new Num with the passed double value.
   * @param value Value of Num
   */
  public Num(double value) {
    super("Number");
    this.value = value;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Val> T castTo(String type) {
    if(type.equals("Number")) {
      return (T) this;
    } else if(type.equals("Boolean")) {
      return (T) new Bool(value >= 0);
    } else if(type.equals("String")) {
      return (T) new Str(String.valueOf(value));
    }

    return null;
  }

  /**
   * Returns the value of this Num as a double.
   * @return Value of Num
   */
  public double getValue() {
    return value;
  }

  public boolean equals(Object obj) {
    if(obj instanceof Num) {
      Num oNum = (Num) obj;

      return value == oNum.getValue();
    }

    return false;
  }

  public String toString() {
    long iValue = (long) value;

    if(iValue == value) {
      return String.valueOf(iValue);
    }

    return String.valueOf(value);
  }
}
