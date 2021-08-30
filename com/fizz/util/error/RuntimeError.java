package com.fizz.util.error;

import com.fizz.interpreter.value.Val;
import com.fizz.lexer.token.*;
import com.fizz.parser.node.Node;
import com.fizz.util.context.Context;
import com.fizz.util.Position;

/**
 * Used for Errors found within the Interpreter related to logic.
 * @author Noah James Rathman
 */
public class RuntimeError extends Error {
  /**
   * Called when a value operation is called with an unexpected value type.
   * @param expt Expected value type
   * @param cause Cause of Error
   * @return Unexpected Type Error
   */
  public static RuntimeError unexpectedType(String expt, Val cause) {
    return new RuntimeError(
      "Unexpected Value Type",
      "Expected " + expt + ", instead got " + cause.getType() + ".",
      cause.getStart(), cause.getEnd(), cause.getContext()
    );
  }

  /**
   * Called when a value tries to use an operation it doesn't have.
   * @param type Type of value
   * @param cause Cause of Error
   * @param context Context of Error
   * @return Unexpected Value Operation
   */
  public static RuntimeError unexpectedOperation(String type, Token cause, Context context) {
    return new RuntimeError(
      "Unexpected Operation",
      type + "s can not use the operation \"" + cause.getType() + "\".",
      cause.getStart(), cause.getEnd(), context
    );
  }

  /**
   * Called when a value is used in an operation it shouldn't.
   * @param opName Illegal operation name
   * @param cause Cause of Error
   * @return Illegal Operation Error
   */
  public static RuntimeError illegalOperation(String opName, Val cause) {
    return new RuntimeError(
      "Illegal Operation",
      cause.getType() + " can not be used in a " + opName + " operation.",
      cause.getStart(), cause.getEnd(), cause.getContext()
    );
  }

  /**
   * Called when a Symbol that doesn't exist is accessed.
   * @param id Name of Symbol
   * @param cause Cause of Error
   * @param context Context of Error
   * @return Undefined Symbol Error
   */
  public static RuntimeError undefinedSymbol(String id, Token cause, Context context) {
    return new RuntimeError(
      "Undefined Symbol",
      "The symbol \"" + id + "\" is undefined.",
      cause.getStart(), cause.getEnd(), context
    );
  }

  /**
   * Called when a the Interpreter tries to reassign a constant Symbol.
   * @param id Name of Symbol
   * @param cause Cause of Error
   * @param context Context of Error
   * @return Illegal Assign Error
   */
  public static RuntimeError illegalAssign(String id, Node cause, Context context) {
    return new RuntimeError(
      "Illegal Assign",
      "The symbol \"" + id + "\" can not be reassigned.",
      cause.getStart(), cause.getEnd(), context
    );
  }

  /**
   * Called when a function is called with too much or too little
   * arguments.
   * @param expt Expected argument size
   * @param actu Actual argument size
   * @param start Start of Error
   * @param end End of Error
   * @param context Context of Error
   * @return Argument Size Error
   */
  public static RuntimeError argumentSize(int expt, int actu, Position start, Position end, Context context) {
    return new RuntimeError(
      "Function Call",
      "Expected " + expt + " arguments, instead got " + actu + ".",
      start, end, context
    );
  }

  /**
   * Called when a function fails to create its SymbolTable.
   * @param start Start of Error
   * @param end End of Error
   * @param context Context of Error
   * @return Table Creation Error
   */
  public static RuntimeError tableCreation(Position start, Position end, Context context) {
    return new RuntimeError(
      "Function Call",
      "Failed to create SymbolTable for function execution.",
      start, end, context
    );
  }

  /**
   * Called when a function attempts to use void as a value.
   * @param cause Cause of Error
   * @param context Context of Error
   * @return Void Value Error
   */
  public static RuntimeError voidValue(Node cause, Context context) {
    return new RuntimeError(
      "Void Value",
      "Value is void.",
      cause.getStart(), cause.getEnd(), context
    );
  }

  /**
   * Called when the program indexes out of the bounds of some
   * list of values.
   * @param idx Indexed position
   * @param size Length of value list
   * @param cause Cause of Error
   * @return Index out of Bounds Error
   */
  public static RuntimeError indexOutOfBounds(int idx, int size, Val cause) {
    return new RuntimeError(
      "Index out of Bounds",
      "Index " + idx + " is out of bounds for length " + size + ".",
      cause.getStart(), cause.getEnd(), cause.getContext()
    );
  }

  //Class
  private Context context;

  /**
   * Creates a new RuntimeError with all details assigned.
   * @param name Name of Error
   * @param details Description of Error
   * @param start Start of Error
   * @param end End of Error
   * @param context Context of Error
   */
  public RuntimeError(String name, String details, Position start, Position end, Context context) {
    super(name, details, start, end);
    this.context = context;
  }

  private String createStackTrace() {
    String trace = "|  Stack Trace (Most recent call last):\n";
    Context con = context;

    while(con.getParent() != null) {
      trace = trace + "|    " + con + "\n";
      con = con.getParent();
    }

    return trace + "|    " + start + " in " + con.getName();
  }

  public String toString() {
    String error = "|  Runtime Error: " + name;
    error += "\n" + createStackTrace();
    error += "\n|  " + createDisplay();
    error += "\n|  " + details;

    return error;
  }
}
