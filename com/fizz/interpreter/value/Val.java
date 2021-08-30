package com.fizz.interpreter.value;

import com.fizz.util.Position;
import com.fizz.util.context.Context;
import com.fizz.util.result.RuntimeResult;

/**
 * Holds all generic info about values.
 * @author Noah James Rathman
 */
public class Val {
  /**
   * Creates and returns a new instance of {@code null} with
   * the passed start, end and context.
   * @param start Start of Val
   * @param end End of Val
   * @param context Context of Val
   * @return New instance of null
   */
  public static Val createNull(Position start, Position end, Context context) {
    Val NULL = new Val("null");
    NULL.setPosition(start, end);
    NULL.setContext(context);

    return NULL;
  }

  /**
   * Creates and returns a new instance void with the passed
   * start, end and context.
   * @param start Start of Val
   * @param end End of Val
   * @param context Context of Val
   * @return New instance of void
   */
  public static Val createVoid(Position start, Position end, Context context) {
    Val VOID = new Val("void");
    VOID.setPosition(start, end);
    VOID.setContext(context);

    return VOID;
  }

  /**
   * Tests if the passed Val self is equal to the Val other.
   * @param self Val representing the Val which called this operation
   * @param other Val operating on this Val
   * @return Result of this operation
   */
  protected static RuntimeResult equalTo(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    Bool result = new Bool(self.equals(other));
    return res.success(result);
  }

  /**
   * Tests if the passed Val self is not equal to the Val other.
   * @param self Val representing the Val which called this operation
   * @param other Val operating on this Val
   * @return Result of this operation
   */
  protected static RuntimeResult notEqualTo(Val self, Val other) {
    RuntimeResult res = new RuntimeResult();
    Bool result = new Bool(!self.equals(other));
    return res.success(result);
  }

  /**
   * Checks if the passed type name exists.
   * @param type Type of value
   * @return True if the passed type name exists, false otherwise.
   */
  public static boolean typeExists(String type) {
    boolean ret = false;

    switch(type) {
      case "Number": ret = true; break;
      case "Boolean": ret = true; break;
      case "String": ret = true; break;
      case "Array": ret = true; break;
      case "Function": ret = true; break;
    }

    return ret;
  }

  //Class
  private String type, attribute;
  /** Start of Val. */
  protected Position start;
  /** End of Val. */
  protected Position end;
  private Context context;

  /**
   * Creates a new Val with the passed type. The generic Val
   * should never be initialized, so this constructor is
   * protected.
   * @param type Type of Val
   */
  protected Val(String type) {
    this.type = type;
    start = null;
    end = null;
    context = null;
    attribute = "none";
  }

  /**
   * Returns if this Val's type matches any of the passed.
   * @param types Wanted Val types
   * @return True if this Val's type is in the passed array
   * of types
   */
  public boolean isType(String...types) {
    for(String type : types) {
      if(this.type.equals(type)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Sets this Val's Position to the passed start and end.
   * @param start Start of Val
   * @param end End of Val
   */
  public void setPosition(Position start, Position end) {
    this.start = start;
    this.end = end;
  }

  /**
   * Sets this Val's Context to the passed Context.
   * @param context Context of Val
   */
  public void setContext(Context context) {
    this.context = context;
  }

  /**
   * Sets this Val's attribute to the passed attribute.
   * @param attribute New Val attribute
   */
  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  /**
   * Casts this value to the passed value type. The default
   * implementation of this method returns null, and should
   * be extended in subclasses for functionality.
   * @param type Type of value
   * @param <T> Return type is determined per call, must extend Val
   * @return Casted value type
   */
  public <T extends Val> T castTo(String type) {
    return null;
  }

  /**
   * Returns the type of this Val.
   * @return Type of Val
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the start of this Val.
   * @return Start of Val
   */
  public Position getStart() {
    return start;
  }

  /**
   * Returns the end of this Val.
   * @return End of Val
   */
  public Position getEnd() {
    return end;
  }

  /**
   * Returns the context of this Val.
   * @return Context of Val
   */
  public Context getContext() {
    return context;
  }

  /**
   * Returns the attribute of this Val.
   * @return String representation of this Val's set attribute.
   * If not attribute is set, null is returned.
   */
  public String getAttribute() {
    return attribute;
  }

  public boolean equals(Object obj) {
    if(obj instanceof Val) {
      Val value = (Val) obj;

      return type.equals(value.getType());
    }

    return false;
  }

  public String toString() {
    return type;
  }
}
