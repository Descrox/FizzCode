package com.fizz.interpreter.value;

import com.fizz.interpreter.RuntimeResult;
import com.fizz.util.Context;
import com.fizz.util.Position;

/**
 * Generic value type and superclass to all other
 * value types.
 * @author Noah James Rathman
 */
public class Value {
	protected Object value;
	protected java.lang.String type;
	protected Position start, end;
	protected Context context;
	
	/**
	 * Creates a new Value with the passed value and type.
	 * @param value		Value of Value
	 * @param type		Type of Value
	 */
	public Value(Object value, java.lang.String type) {
		this.value = value;
		this.type = type;
	}
	
	/**
	 * Sets the Position of this Value to the passed <b>start</b>
	 * and <b>end</b>.
	 * @param start		Start Position of Value
	 * @param end		End Position of Value
	 */
	public void setPosition(Position start, Position end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Sets the Context of this Value to the passed <b>context</b>.
	 * @param context		Context of Value
	 */
	public void setContext(Context context) {
		this.context = context;
	}
	
	/**
	 * Returns the value of this Value.
	 * @return Value of Value	
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns the type of this Value.
	 * @return Type of Value
	 */
	public java.lang.String getType() {
		return type;
	}
	
	/**
	 * Returns the starting Position of this Value.
	 * @return Start Position of Value
	 */
	public Position getStart() {
		return start;
	}
	
	/**
	 * Returns the ending Position of this Value.
	 * @return End Position of Value
	 */
	public Position getEnd() {
		return end;
	}
	
	/**
	 * Returns the Context of this Value.
	 * @return Context of Value
	 */
	public Context getContext() {
		return context;
	}
	
	public RuntimeResult equalTo(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		Boolean bool = new Boolean(value.equals(other.getValue()) && type.equals(other.getType()));
		bool.setPosition(start, other.getEnd());
		bool.setContext(other.getContext());
		
		return res.success(bool);
	}
	
	public RuntimeResult notEqualTo(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		Boolean bool = new Boolean(!value.equals(other.getValue()) || !type.equals(other.getType()));
		bool.setPosition(start, other.getEnd());
		bool.setContext(other.getContext());
		
		return res.success(bool);
	}
	
	public java.lang.String toString() {
		return value.toString();
	}
}