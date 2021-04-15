package com.fizz.util.error;

import com.fizz.util.Context;
import com.fizz.util.Position;

/**
 * Returned when the {@link Interpreter} finds an Error at runtime.
 * @author Noah James Rathman
 */
public class RuntimeError extends Error {
	private Context context;
	
	/**
	 * Creates a new RuntimeError with the passed details, start and end Position, and context.
	 * @param details		Details of Error
	 * @param start			Start Position of Error
	 * @param end			End Position of Error
	 * @param context		Context of Error
	 */
	public RuntimeError(String details, Position start, Position end, Context context) {
		super("Runtime", details, start, end);
		this.context = context;
	}
	
	/**
	 * Generates a trace back message telling exactly where the Error was found.
	 * @return Error trace back message
	 */
	private String generateTraceback() {
		String trace = start + " in " + context.getName();
		Context con = context.getParent();
		
		while(con != null) {
			trace = con.getParentEntryPosition() + " in " + context.getName() + "\n" + trace;
			con = con.getParent();
		}
		
		return "Traceback (most recent call last):\n" + trace;
	}
	
	public String toString() {
		String err = "Error: " + name;
		err += "\n" + generateTraceback();
		err += "\n" + pointTo();
		err += "\nDetails: " + details;
		
		return err;
	}
}