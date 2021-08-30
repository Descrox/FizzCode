package com.fizz.util.context;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import com.fizz.interpreter.value.*;

/**
 * Holds a Map of Symbols for a specific Context as well as
 * it's parent.
 * @author Noah James Rathman
 */
public class SymbolTable {
  /**
   * Creates a new instance of the Global Symbol Table.
   * @return Global Symbol Table
   */
  public static SymbolTable createGST() {
    SymbolTable sym = new SymbolTable();
    sym.set("null",           Val.createNull(null, null, null), true);
    sym.set("true",           new Bool(true), true);
    sym.set("false",          new Bool(false), true);
    sym.set("print",          SysFunc.getFunction("print"), true);
    sym.set("println",        SysFunc.getFunction("println"), true);
    sym.set("input",          SysFunc.getFunction("input"), true);
    sym.set("typeOf",         SysFunc.getFunction("typeOf"), true);
    sym.set("length",         SysFunc.getFunction("length"), true);
    sym.set("run",            SysFunc.getFunction("run"), true);

    return sym;
  }

  //Class
  private SymbolTable parent;
  private Map<String, Symbol> symbols;

  /**
   * Creates a new SymbolTable with no parent.
   */
  public SymbolTable() {
    parent = null;
    symbols = new HashMap<String, Symbol>();
  }

  /**
   * Creates a new SymbolTable with the assigned parent.
   * @param parent Other SymbolTable of which this one can pull
   * Vals from
   */
  public SymbolTable(SymbolTable parent) {
    this.parent = parent;
    symbols = new HashMap<String, Symbol>();
  }

  /**
   * Attempts to assign a Symbol with the passed id.
   * @param id Name used to identify Symbol
   * @param value Value assigned to Symbol
   * @param constant Determines if Symbol is assigned constant
   * @return This method returns true if the Symbol is
   * successfully assigned, otherwise false
   */
  public boolean set(String id, Val value, boolean constant) {
    Symbol sym = symbols.get(id);

    if(sym == null || !sym.isConstant()) {
      symbols.put(id, new Symbol(value, constant));
      return true;
    }

    return false;
  }

  /**
   * Assigns a Symbol with the passed id regardless of if it's
   * constant.
   * @param id Name used to identify Symbol
   * @param value Value assigned to Symbol
   * @param constant Determines if Symbol is assigned constant
   */
  public void setIgnoreConstant(String id, Val value, boolean constant) {
    symbols.put(id, new Symbol(value, constant));
  }

  /**
   * Returns the value held with the assigned id.
   * @param id Target Symbol id
   * @return The value of the assigned Symbol with the passed
   * id. If no Symbol exists, this method returns null
   */
  public Symbol get(String id) {
    Symbol sym = symbols.get(id);

    if(sym == null && parent != null) {
      sym = parent.get(id);
    }

    return sym;
  }

  /**
   * Removes the Symbol associated with the passed id.
   * @param id Target Symbol id
   */
  public void remove(String id) {
    symbols.remove(id);
  }

  /**
   * Returns all Symbols in this SymbolTable as a list of displays.
   * <i>(ID: SYMBOL-NAME Type: SYMBOL-VALUE-TYPE Value: SYMBOL-VALUE)</i>
   * @return List of Symbol displays
   */
  public List<String> getDisplayList() {
    List<Symbol> syms = new ArrayList<Symbol>(symbols.values());
    List<String> ids = new ArrayList<String>(symbols.keySet());
    List<String> displays = new ArrayList<String>();

    for(int i = 0; i < syms.size(); i++) {
      Symbol sym = syms.get(i);
      String id = "ID: " + ids.get(i);
      String disp = id + "\n|  Type: " + sym.getValue().getType() + "\n|  Value: " + sym.getValue();
      displays.add(disp);
    }

    Collections.sort(displays);
    return displays;
  }
}
