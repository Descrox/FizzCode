package com.fizz.lexer;

import java.util.List;
import java.util.ArrayList;

import com.fizz.lexer.token.*;
import com.fizz.util.Position;
import com.fizz.util.Constants;
import com.fizz.util.error.Error;
import com.fizz.util.error.LexerError;

/**
 * Splits the input Fizz code into a list of {@link com.fizz.lexer.token.Token}s.
 * @author Noah James Rathman
 */
public final class Lexer {
  private static Position pos;
  private static Error error;
  private static String text;
  private static char curChar;

  //Lexer should never be instanced.
  private Lexer() {}

  /**
   * Returns if the Lexer is holding an Error.
   * @return True if the Lexer Error doesn't equal null
   */
  public static boolean hasError() {
    return error != null;
  }

  /**
   * Returns whatever Error the Lexer is currently holding.
   * @return Lexer Error
   */
  public static Error getError() {
    return error;
  }

  /**
   * Resets the Lexer and prepares it to read the passed text.
   * @param fName File name
   * @param fText File text
   */
  public static void init(String fName, String fText) {
    pos = new Position(fName, fText);
    error = null;
    text = fText;
    curChar = 0;

    advance();
  }

  private static void advance() {
    pos.advance(curChar);

    curChar = (pos.getIndex() < text.length() ? text.charAt(pos.getIndex()) : 0);
  }

  /**
   * Creates a list of {@link com.fizz.lexer.token.Token}s from the passed Fizz
   * code and returns them.
   * @return A List of Tokens to be used in the Parser
   */
  public static List<Token> createTokenList() {
    List<Token> tokens = new ArrayList<Token>();
    Position start;

    while(curChar != 0) {
      start = pos.copy();

      if(curChar == ' ' || curChar == '\t' || curChar == '\n') {
        advance();
      } else if(Character.isDigit(curChar)) {
        Token num = makeNumber();
        if(num == null) {return null;}

        tokens.add(num);
      } else if(Character.isAlphabetic(curChar) || curChar == '_') {
        tokens.add(makeIdentifier());
      } else if(Constants.MATCHABLE.indexOf(curChar) != -1) {
        Type type = Type.getMatchingType(String.valueOf(curChar));
        advance();

        tokens.add(new Token(type, start, pos.copy()));
      } else if(Constants.EQUALABLE.indexOf(curChar) != -1) {
        tokens.add(makeEquals());
      } else if(curChar == '\"') {
        Token str = makeString();
        if(str == null) {return null;}

        tokens.add(str);
      } else {
        char cur = curChar;
        advance();

        error = LexerError.illegalCharacter(cur, start, pos.copy());
        return null;
      }
    }

    start = pos.copy();
    advance();
    tokens.add(new Token(Type.EOF, start, pos.copy()));

    return tokens;
  }

  private static Token makeNumber() {
    String num = "";
    Position start = pos.copy();
    boolean flo = false;

    while(curChar != 0 && (Character.isDigit(curChar) || curChar == '.')) {
      if(curChar == '.') {
        if(flo) {
          start = pos.copy();
          advance();

          error = LexerError.numberFormat(start, pos.copy());
          return null;
        }

        flo = true;
      }

      num += curChar;
      advance();
    }

    Type type = flo ? Type.FLOAT : Type.INT;
    return new Token(type, num, start, pos.copy());
  }

  private static Token makeString() {
    String str = "";
    Position start = pos.copy();

    advance();
    while(true) {
      if(curChar == '\"') {
        break;
      } else if(curChar == '\\') {
        advance();

        if(curChar == '\\') {
          str += "\\";
        } else if(curChar == '"') {
          str += "\"";
        } else if(curChar == 'n') {
          str += "\n";
        } else if(curChar == 't') {
          str += "\t";
        } else {
          char cur = curChar;
          start = pos.copy();
          advance();

          error = LexerError.illegalEscape(cur, start, pos.copy());
          return null;
        }

        advance();
        continue;
      } else if(curChar == 0) {
        start = pos.copy();
        advance();

        error = LexerError.unclosedString(start, pos.copy());
        return null;
      }

      str += curChar;
      advance();
    }

    advance();
    return new Token(Type.STR, str, start, pos.copy());
  }

  private static Token makeIdentifier() {
    String id = "";
    Position start = pos.copy();

    while(Character.isAlphabetic(curChar) || Character.isDigit(curChar) || curChar == '_') {
      id += curChar;
      advance();
    }

    Type type = Constants.isKey(id) ? Type.KEY : Type.ID;
    return new Token(type, id, start, pos.copy());
  }

  private static Token makeEquals() {
    String op = "";
    Position start = pos.copy();
    int count = 0;

    do {
      op += curChar;
      advance();
      count++;
    } while(curChar == '=' && count < 2);

    Type type = Type.getMatchingType(op);
    return new Token(type, start, pos.copy());
  }
}
