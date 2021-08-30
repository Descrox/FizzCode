package com.fizz.util.error;

import com.fizz.util.Position;
import com.fizz.lexer.token.*;

/**
 * Used for Errors found within the Parser related to syntax.
 * @author Noah James Rathman
 */
public final class InvalidSyntaxError extends Error {
  /**
   * Called when the Parser doesn't find a Token Type it was expecting.
   * @param actu Found Token
   * @param expt Expected Types
   * @return Expected Token Type Error
   */
  public static InvalidSyntaxError expectedTokenType(Token actu, Type...expt) {
    String details = "Expected ";
    String msg = "";

    for(int i = 0; i < expt.length; i++) {
      msg = "\"" + expt[i] + "\"";
      msg += (i + 1 < expt.length ? ", " : ". ");
      details += msg;
    }

    details += "Instead got \"" + actu + "\".";

    return new InvalidSyntaxError(
      details, actu.getStart(), actu.getEnd()
    );
  }

  /**
   * Called when the Parser finds a Token it wasn't expecting.
   * @param actu Found Token
   * @param expt Expected Token
   * @return Expected Token Error
   */
  public static InvalidSyntaxError expectedToken(Token actu, Token expt) {
    return new InvalidSyntaxError(
      "Expected Token \"" + expt + "\", instead got \"" + actu + "\".",
      actu.getStart(), actu.getEnd()
    );
  }

  //Class
  /**
   * Creates a new InvalidSyntaxError with the passed details, start and end.
   * @param details Description of Error
   * @param start Start of Error
   * @param end End of Error
   */
  public InvalidSyntaxError(String details, Position start, Position end) {
    super("Invalid Syntax", details, start, end);
  }
}
