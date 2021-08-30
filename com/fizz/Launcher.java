package com.fizz;

import java.util.Scanner;
import java.util.List;

import com.fizz.lexer.Lexer;
import com.fizz.lexer.token.Token;
import com.fizz.parser.Parser;
import com.fizz.interpreter.Interpreter;
import com.fizz.util.Constants;
import com.fizz.util.result.ParseResult;
import com.fizz.util.result.RuntimeResult;
import com.fizz.util.context.*;

/**
 * Used primarily to launch the shell of Fizz.
 * Additionally contains the compilation process,
 * which can be used in programs outside the shell.
 * @author Noah James Rathman
 */
public final class Launcher {
  //Launcher should never be instanced.
  private Launcher() {}

  /**
   * Creates the Fizz shell.
   */
  public static void createShell() {
    Constants.init();
    Scanner in = new Scanner(System.in);
    String input = "";

    while(true) {
      System.out.print("Fizz > ");
      input = in.nextLine();

      if(input.equals("/exit")) {break;}
      else if(input.startsWith("/help")) {
        helpDialog(input.replace("/help", ""));
        continue;
      } else if(input.equals("")) {continue;}

      run("<shell>", input, Constants.PROGRAM);
    }

    System.out.println("Goodbye.");
  }

  private static void helpDialog(String type) {
    String command = type.replace(" ", "");
    String repr = null;

    System.out.println("---------------------------------------------");
    switch(command) {
      case "sysSymbols":
        repr = "System Symbols:\n" + sysSymbols();
        break;
      case "":
        repr = "Expected command after \"/help\".\n";
        break;
      default:
        repr = "Illegal help command \"" + command + "\".\n";
        break;
    }

    System.out.println(repr);
  }

  private static String sysSymbols() {
    List<String> displays = SymbolTable.createGST().getDisplayList();
    String repr = "";

    for(int i = 0; i < displays.size(); i++) {
      repr += displays.get(i) + (i + 1 < displays.size() ? "\n\n" : "\n");
    }

    return repr;
  }

  /**
   * Compiles and executes the passed Fizz code.
   * @param fName Name of Fizz file
   * @param fText Content of Fizz file
   * @param context Base context of program
   */
  public static void run(String fName, String fText, Context context) {
    //Lexer
    Lexer.init(fName, fText.replace("\r", ""));
    List<Token> tokens = Lexer.createTokenList();
    if(Lexer.hasError()) {
      System.out.println("\n" + Lexer.getError() + "\n");
      return;
    }

    //Parser
    Parser.init(tokens);
    ParseResult pRes = Parser.parse();
    if(pRes.hasError()) {
      System.out.println("\n" + pRes.getError() + "\n");
      return;
    }

    //Interpreter
    RuntimeResult iRes = Interpreter.visit(pRes.getValue(), context);
    if(iRes.hasError()) {
      System.out.println("\n" + iRes.getError() + "\n");
      return;
    }

    System.out.print("\n");
  }

  public static void main(String[] args) {
    createShell();
  }
}
