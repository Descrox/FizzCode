package com.fizz.lexer.token;

import com.fizz.util.Position;

/**
 * Used in the Parser for the construction of Nodes.
 *
 * @author Noah James Rathman
 */
public final class Token {
  private Type type;
  private String value;
  private Position start, end;

  /**
   * Creates a Token with no start or end.
   * @param type Type of Token
   * @param value Value of Token
   */
  public Token(Type type, String value) {
    this.type = type;
    this.value = value;
    start = null;
    end = null;
  }

  /**
   * Creates a Token with no value.
   * @param type Type of Token
   * @param start Start of Token
   * @param end End of Token
   */
  public Token(Type type, Position start, Position end) {
    this.type = type;
    this.start = start;
    this.end = end;
    value = null;
  }

  /**
   * Creates a Token with all values assigned.
   * @param type Type of Token
   * @param value Value of Token
   * @param start Start of Token
   * @param end End of Token
   */
  public Token(Type type, String value, Position start, Position end) {
    this.type = type;
    this.value = value;
    this.start = start;
    this.end = end;
  }

  /**
   * Returns if this Tokens type is inside the passed
   * array of {@link com.fizz.lexer.token.Type}s.
   * @param types Wanted types
   * @return If this Tokens type matches any of the
   * passed types.
   */
  public boolean isType(Type...types) {
    for(Type type : types) {
      if(type == this.type) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns if this Tokens value is inside the passed
   * array of Strings.
   * @param values Wanted values
   * @return If this Tokens value matches any of the
   * passed values.
   */
  public boolean hasValue(String...values) {
    for(String value : values) {
      if(value.equals(this.value)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the type of this Token.
   * @return Token type
   */
  public Type getType() {
    return type;
  }

  /**
   * Returns the value of this Token.
   * @return Token value
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns the start of this Token.
   * @return Token start
   */
  public Position getStart() {
    return start;
  }

  /**
   * Returns the end of this Token.
   * @return Token end
   */
  public Position getEnd() {
    return end;
  }

  public String toString() {
    return value != null ? type + ":" + value : type.toString();
  }
}
