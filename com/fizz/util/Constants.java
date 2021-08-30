package com.fizz.util;

import com.fizz.interpreter.Interpreter;
import com.fizz.interpreter.value.*;
import com.fizz.util.context.Context;
import com.fizz.util.context.SymbolTable;

/**
 * Contains many useful constant values.
 * @author Noah James Rathman
 */
public final class Constants {
  /** Contains all matchable single symbols. */
  public static final String MATCHABLE = "+-*/()&|,{};[]";
  /** Contains all symbols that can be followed by a "=". */
  public static final String EQUALABLE = "!=<>";
  /** Contains all Fizz keywords. */
  public static final String[] KEYWORDS = {
    "var", "const", "cast", "func", "return",
    "if", "elif", "else", "while", "break",
    "continue", "for"
  };

  /** The base Context of all Fizz programs. */
  public static Context PROGRAM;

  /**
   * Initializes all constant values inside and outside the Constants class.
   */
  public static void init() {
    initClassConstants();
    PROGRAM = new Context("<program>", SymbolTable.createGST());
  }

  private static void initClassConstants() {
    Interpreter.init();
    Num.init();
    Str.init();
    Bool.init();
    Array.init();
    SysFunc.init();
  }

  /**
   * Checks if the passed String matches a keyword.
   * @param id Identifier being checked
   * @return True if the passed String matches any String inside
   * of {@link #KEYWORDS}, false otherwise
   */
  public static boolean isKey(String id) {
    for(String key : KEYWORDS) {
      if(key.equals(id)) {
        return true;
      }
    }

    return false;
  }

  //Constants should never be instanced.
  private Constants() {}
}
