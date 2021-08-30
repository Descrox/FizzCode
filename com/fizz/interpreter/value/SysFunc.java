package com.fizz.interpreter.value;

import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.fizz.Launcher;
import com.fizz.interpreter.Interpreter;
import com.fizz.util.*;
import com.fizz.util.context.*;
import com.fizz.util.result.RuntimeResult;
import com.fizz.util.function.SFunc;
import com.fizz.util.error.RuntimeError;

/**
 * Holds functions available in all Fizz programs.
 * @author Noah James Rathman
 */
public class SysFunc extends BaseFunc {
  private static Map<String, SysFunc> FUNCTIONS;

  /**
   * Initializes all System Functions.
   */
  public static void init() {
    FUNCTIONS = new HashMap<String, SysFunc>();

    //print
    FUNCTIONS.put("print", new SysFunc(
      "print",
      Arrays.asList(new String[] {"msg"}),
      SysFunc::PRINT
    ));

    //println
    FUNCTIONS.put("println", new SysFunc(
      "println",
      Arrays.asList(new String[] {"msg"}),
      SysFunc::PRINTLN
    ));

    //input
    FUNCTIONS.put("input", new SysFunc(
      "input",
      Arrays.asList(new String[] {"msg"}),
      SysFunc::INPUT
    ));

    //typeOf
    FUNCTIONS.put("typeOf", new SysFunc(
      "typeOf",
      Arrays.asList(new String[] {"value"}),
      SysFunc::TYPE_OF
    ));

    //length
    FUNCTIONS.put("length", new SysFunc(
      "length",
      Arrays.asList(new String[] {"value"}),
      SysFunc::LENGTH
    ));

    //run
    FUNCTIONS.put("run", new SysFunc(
      "run",
      Arrays.asList(new String[] {"path"}),
      SysFunc::RUN
    ));
  }

  /**
   * Returns the System Function associated with the passed
   * name.
   * @param name Name of function
   * @return SysFunc associated with passed name if any, otherwise
   * returns null
   */
  public static SysFunc getFunction(String name) {
    return FUNCTIONS.get(name);
  }

  private static RuntimeResult PRINT(Position entry, Context context) {
    RuntimeResult res = new RuntimeResult();
    Symbol sym = context.getSymbolTable().get("msg");

    System.out.print(sym.getValue());

    return res.success(Val.createNull(null, null, null));
  }

  private static RuntimeResult PRINTLN(Position entry, Context context) {
    RuntimeResult res = new RuntimeResult();
    Symbol sym = context.getSymbolTable().get("msg");

    System.out.println(sym.getValue());

    return res.success(Val.createNull(null, null, null));
  }

  private static RuntimeResult INPUT(Position entry, Context context) {
    RuntimeResult res = new RuntimeResult();
    Symbol sym = context.getSymbolTable().get("msg");
    System.out.print(sym.getValue());

    Scanner in = new Scanner(System.in);
    Str input = new Str(in.nextLine());
    input.setAttribute("returned");

    return res.success(input);
  }

  private static RuntimeResult TYPE_OF(Position entry, Context context) {
    RuntimeResult res = new RuntimeResult();
    Symbol sym = context.getSymbolTable().get("value");

    Str typeName = new Str(sym.getValue().getType());
    typeName.setAttribute("returned");

    return res.success(typeName);
  }

  private static RuntimeResult LENGTH(Position entry, Context context) {
    RuntimeResult res = new RuntimeResult();
    Symbol sym = context.getSymbolTable().get("value");
    Val sVal = sym.getValue();

    Num len = null;
    if(sVal.isType("String")) {
      Str str = (Str) sVal;
      len = new Num(str.getValue().length());
    } else if(sVal.isType("Array")) {
      Array arr = (Array) sVal;
      len = new Num(arr.getValues().size());
    } else {
      return res.failure(RuntimeError.unexpectedType("String or Array", sVal));
    }

    len.setAttribute("returned");
    return res.success(len);
  }

  private static RuntimeResult RUN(Position entry, Context context) {
    RuntimeResult res = new RuntimeResult();
    Symbol sym = context.getSymbolTable().get("path");

    Val value = sym.getValue();
    if(!value.isType("String")) {
      return res.failure(RuntimeError.unexpectedType("String", value));
    }

    String path = ((Str) value).getValue();
    File file = new File(path);
    if(!file.exists()) {
      return res.failure(new RuntimeError(
        "File Access",
        "The file \"" + path + "\" does not exist.",
        value.getStart(), value.getEnd(), context
      ));
    } else if(!path.endsWith(".fizz")) {
      return res.failure(new RuntimeError(
        "File Access",
        "Accessed file should have the extension \".fizz\".",
        value.getStart(), value.getEnd(), context
      ));
    }

    String fName = file.getName().replace(".fizz", "");
    String fText = "";

    try {
      FileInputStream fiStream = new FileInputStream(file);
      int i = 0;

      while((i = fiStream.read()) != -1) {
        fText += (char) i;
      }
    } catch(IOException e) {
      return res.failure(new RuntimeError(
        "Native Exception",
        "IOException: " + e.getMessage(),
        value.getStart(), value.getEnd(), context
      ));
    }

    Context fProgram = new Context("<program>", SymbolTable.createGST(), context, entry);
    Launcher.run(fName, fText, fProgram);
    return res.success(Val.createNull(null, null, null));
  }

  //Class
  private SFunc func;

  private SysFunc(String name, List<String> argNames, SFunc func) {
    super(name, argNames);
    this.func = func;
  }

  @Override
  public RuntimeResult execute(List<Val> argValues, Context parent) {
    RuntimeResult res = new RuntimeResult();
    if(argNames.size() != argValues.size()) {
      return res.failure(RuntimeError.argumentSize(argNames.size(), argValues.size(), start, end, parent));
    }

    SymbolTable fSymTable = populateArgs(argValues, parent.getSymbolTable());
    if(fSymTable == null) {
      return res.failure(RuntimeError.tableCreation(start, end, parent));
    }
    Context fContext = new Context("<Function " + name + ">", fSymTable, parent, start);

    Val result = res.register(func.run(start, fContext));
    if(res.hasError()) {return res;}

    return res.success(result);
  }

  public String toString() {
    return "<System Function: " + name + ">";
  }
}
