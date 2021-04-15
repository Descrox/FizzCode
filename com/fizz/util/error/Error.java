package com.fizz.util.error;

import com.fizz.util.Position;

/**
 * Handles the creation and display of Error messages.
 * @author Noah James Rathman
 */
public class Error {
	protected String name, details;
	protected Position start, end;
	
	/**
	 * Creates a new Error with the passed name, details, start and end position.
	 * Constructor is protected because the base Error class should only be extended.
	 * @param name		Name of Error
	 * @param details	Details of Error
	 * @param start		Start Position of Error
	 * @param end		End Position of Error
	 */
	protected Error(String name, String details, Position start, Position end) {
		this.name = name;
		this.details = details;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Returns the current line of the Error.
	 */
	private String getLine() {
		String full = start.getFileText();
		int sIdx = start.getIndex(), eIdx = end.getIndex();
		
		if(sIdx < full.length()) {
			char cur = 0;
			while(sIdx != 0 && (cur = full.charAt(sIdx)) != '\n') {
				sIdx--;
			}
			
			if(cur == '\n') {sIdx++;}
			
			while(eIdx != full.length() && (cur = full.charAt(eIdx)) != '\n') {
				eIdx++;
			}
			
			if(cur == '\n') {eIdx--;}
			
			return full.substring(sIdx, eIdx);
		} else {
			return full;
		}
	}
	
	/**
	 * Creates a visual representation of where the Error was found.
	 */
	protected String pointTo() {
		String line = getLine();
		
		int sIdx = start.getColumn() - 1, eIdx = end.getColumn() - 1;
		
		String point = "";
		for(int i = 0; i < eIdx; i++) {
			if(i >= sIdx) {
				point += "^";
			} else {
				point += " ";
			}
		}
		
		return line + "\n" + point;
	}
	
	public String toString() {
		String err = "Error: " + name;
		err += "\n" + start;
		err += "\n" + pointTo();
		err += "\nDetails: " + details;
		
		return err;
	}
}