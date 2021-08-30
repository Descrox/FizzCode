package com.fizz.util.error;

import com.fizz.util.Position;

/**
 * Contains predefined Errors that can be created in the {@link com.fizz.lexer.Lexer}.
 * @author Noah James Rathman
 */
public final class LexerError extends Error {
  /**
   * Used when the Lexer finds an illegal character.
   * @param c Illegal char
   * @param start Start of Error
   * @param end End of Error
   * @return Illegal Character Error
   */
  public static LexerError illegalCharacter(char c, Position start, Position end) {
    return new LexerError(
      "Illegal Character",
      "The character \"" + c + "\" isn't recognized.",
      start, end
    );
  }

  /**
   * Used when the Lexer tries to create a number with multiple decimals.
   * @param start Start of Error
   * @param end End of Error
   * @return Number Format Error
   */
  public static LexerError numberFormat(Position start, Position end) {
    return new LexerError(
      "Number Format",
      "Numbers cannot contain multiple decimals.",
      start, end
    );
  }

  /**
   * Used when the Lexer tries to create a string that isn't closed.
   * @param start Start of Error
   * @param end End of Error
   * @return Unclosed String Error
   */
  public static LexerError unclosedString(Position start, Position end) {
    return new LexerError(
      "Unclosed String",
      "Expected a closing \".",
      start, end
    );
  }

  /**
   * Used when the Lexer tries to create an invalid escape character.
   * @param curChar Invalid escape charaacter
   * @param start Start of Error
   * @param end End of Error
   * @return Illegal Escape Character Error
   */
  public static LexerError illegalEscape(char curChar, Position start, Position end) {
    return new LexerError(
      "Illegal Escape Character",
      "\"\\" + curChar + "\" is not a valid escape character.",
      start, end
    );
  }

  //Class
  private LexerError(String name, String details, Position start, Position end) {
    super(name, details, start, end);
  }
}
