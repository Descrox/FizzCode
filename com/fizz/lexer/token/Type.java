package com.fizz.lexer.token;

/**
 * Used in tandem with {@link com.fizz.lexer.token.Token}s to help identify their use.
 * @author Noah James Rathman
 */
public enum Type {
  /** Represents an integer. */
  INT("int"),
  /** Represents a float. */
  FLOAT("float"),
  /** Represents a string. */
  STR("string"),

  /** Represents a "+". */
  ADD("+"),
  /** Represents a "-". */
  SUB("-"),
  /** Represents a "*". */
  MUL("*"),
  /** Represents a "/". */
  DIV("/"),
  /** Represents a "=". */
  EQ("="),
  /** Represents a "==". */
  EE("=="),
  /** Represents a "!". */
  NOT("!"),
  /** Represents a "!=". */
  NE("!="),
  /** Represents a "&lt;". */
  LT("<"),
  /** Represents a "&lt;=". */
  LTE("<="),
  /** Represents a ">". */
  GT(">"),
  /** Represents a ">=". */
  GTE(">="),
  /** Represents a "&amp;". */
  AND("&"),
  /** Represents a "|". */
  OR("|"),

  /** Represents a "(". */
  LPAREN("("),
  /** Represents a ")". */
  RPAREN(")"),
  /** Represents a """. */
  QUOTE("\""),
  /** Represents a ",". */
  COMMA(","),
  /** Represents a "{". */
  LBRACE("{"),
  /** Represents a "}". */
  RBRACE("}"),
  /** Represents a ";". */
  EOL(";"),
  /** Represents a "[". */
  LSQUARE("["),
  /** Represents a "]". */
  RSQUARE("]"),

  /** Represents an identifier. */
  ID("id"),
  /** Represents a keyword. */
  KEY("key"),
  /** Represents the end of the file. */
  EOF("EoF");

  //Class
  private String repr;

  private Type(String repr) {
    this.repr = repr;
  }

  /**
   * Returns the Type which has the matching repr value.
   * @param repr Value to match
   * @return Type which matches the passed repr value. If
   * none match that value, {@code null} is returned.
   */
  public static Type getMatchingType(String repr) {
    final Type[] ALL = Type.values();

    for(Type t : ALL) {
      if(t.repr.equals(repr)) {
        return t;
      }
    }

    return null;
  }

  public String toString() {
    return repr;
  }
}
