package com.fizz.util.context;

import com.fizz.util.Position;

/**
 * Contains extra information for specific positions in code.
 * @author Noah James Rathman
 */
public class Context {
  private Context parent;
  private Position entry;
  private SymbolTable table;
  private String name;

  /**
   * Creates a new Context without a parent.
   * @param name Name of Context
   * @param table SymbolTable of Context
   */
  public Context(String name, SymbolTable table) {
    this.name = name;
    this.table = table;
    parent = null;
    this.entry = null;
  }

  /**
   * Creates a new Context with a parent.
   * @param name Name of Context
   * @param table SymbolTable of Context
   * @param parent Parent of Context
   * @param entry Position where this Context was created
   */
  public Context(String name, SymbolTable table, Context parent, Position entry) {
    this.name = name;
    this.table = table;
    this.parent = parent;
    this.entry = entry;
  }

  /**
   * Returns the name of this Context.
   * @return Name of Context
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the SymbolTable of this Context.
   * @return SymbolTable of Context
   */
  public SymbolTable getSymbolTable() {
    return table;
  }

  /**
   * Returns the parent of this Context.
   * @return Parent of Context
   */
  public Context getParent() {
    return parent;
  }

  /**
   * Returns the entry Position of this Context's parent.
   * @return Entry point of parent
   */
  public Position getEntry() {
    return entry;
  }

  public String toString() {
    return entry + " in " + name;
  }
}
