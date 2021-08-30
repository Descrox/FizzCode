package com.fizz.util.context;

import com.fizz.interpreter.value.Val;

/**
 * Holds a Val used in a SymbolTable.
 * @author Noah James Rathman
 */
public class Symbol {
  private Val value;
  private boolean constant;

  /**
   * Creates a new Symbol with the passed Val and also determines
   * if it's constant.
   * @param value Value held by Symbol
   * @param constant Determines if the Symbol can be changed or not
   */
  public Symbol(Val value, boolean constant) {
    this.value = value;
    this.constant = constant;
  }

  /**
   * Returns the value of this Symbol.
   * @return Value held by Symbol
   */
  public Val getValue() {
    return value;
  }

  /**
   * Returns if this Symbol is constant or not.
   * @return If Symbol is constant
   */
  public boolean isConstant() {
    return constant;
  }
}
