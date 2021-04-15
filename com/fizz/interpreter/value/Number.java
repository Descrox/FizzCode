package com.fizz.interpreter.value;

import com.fizz.interpreter.RuntimeResult;
import com.fizz.util.error.RuntimeError;

public class Number extends Value {
	public Number(Object value) {
		super(value, "Number");
	}
	
	public RuntimeResult add(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		if(other.getType().equals("Number")) {
			Double num1 = ((java.lang.Number) value).doubleValue();
			Double num2 = ((java.lang.Number) other.getValue()).doubleValue();
			Double result = num1 + num2;
			
			Number num;
			if(result > Math.floor(result)) {
				num = new Number(result);
			} else {
				num = new Number(result.longValue());
			}
			
			num.setPosition(start, other.getEnd());
			num.setContext(other.getContext());
			
			return res.success(num);
		}
		
		return res.failure(new RuntimeError(
			other.getType() + "s cannot be added to Numbers.",
			other.getStart(), other.getEnd(), other.getContext()
		));
	}
	
	public RuntimeResult subtract(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		if(other.getType().equals("Number")) {
			Double num1 = ((java.lang.Number) value).doubleValue();
			Double num2 = ((java.lang.Number) other.getValue()).doubleValue();
			Double result = num1 - num2;
			
			Number num;
			if(result > Math.floor(result)) {
				num = new Number(result);
			} else {
				num = new Number(result.longValue());
			}
			
			num.setPosition(start, other.getEnd());
			num.setContext(other.getContext());
			
			return res.success(num);
		}
		
		return res.failure(new RuntimeError(
			other.getType() + "s cannot be subtracted by Numbers.",
			other.getStart(), other.getEnd(), other.getContext()
		));
	}
	
	public RuntimeResult multiply(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		if(other.getType().equals("Number")) {
			Double num1 = ((java.lang.Number) value).doubleValue();
			Double num2 = ((java.lang.Number) other.getValue()).doubleValue();
			Double result = num1 * num2;
			
			Number num;
			if(result > Math.floor(result)) {
				num = new Number(result);
			} else {
				num = new Number(result.longValue());
			}
			
			num.setPosition(start, other.getEnd());
			num.setContext(other.getContext());
			
			return res.success(num);
		}
		
		return res.failure(new RuntimeError(
			other.getType() + "s cannot be multiplied by Numbers.",
			other.getStart(), other.getEnd(), other.getContext()
		));
	}
	
	public RuntimeResult divide(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		if(other.getType().equals("Number")) {
			Double num1 = ((java.lang.Number) value).doubleValue();
			Double num2 = ((java.lang.Number) other.getValue()).doubleValue();
			if(num2 == 0) {
				return res.failure(new RuntimeError(
						"Division by Zero.",
						other.getStart(), other.getEnd(), other.getContext()
				));
			}
			
			Double result = num1 / num2;
			
			Number num;
			if(result > Math.floor(result)) {
				num = new Number(result);
			} else {
				num = new Number(result.longValue());
			}
			
			num.setPosition(start, other.getEnd());
			num.setContext(other.getContext());
			
			return res.success(num);
		}
		
		return res.failure(new RuntimeError(
			other.getType() + "s cannot be divided by Numbers.",
			other.getStart(), other.getEnd(), other.getContext()
		));
	}

	public RuntimeResult lessThan(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		if(!other.getType().equals("Number")) {
			return res.failure(new RuntimeError(
				other.getType() + "s are not comparable with Numbers.",
				other.getStart(), other.getEnd(), other.getContext()
			));
		}
		
		Double num1 = ((java.lang.Number) value).doubleValue();
		Double num2 = ((java.lang.Number) other.getValue()).doubleValue();
		
		Boolean bool = new Boolean(num1 < num2);
		bool.setPosition(start, other.getEnd());
		bool.setContext(other.getContext());
		
		return res.success(bool);
	}
	
	public RuntimeResult lessThanOrEqualTo(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		if(!other.getType().equals("Number")) {
			return res.failure(new RuntimeError(
				other.getType() + "s are not comparable with Numbers.",
				other.getStart(), other.getEnd(), other.getContext()
			));
		}
		
		Double num1 = ((java.lang.Number) value).doubleValue();
		Double num2 = ((java.lang.Number) other.getValue()).doubleValue();
		
		Boolean bool = new Boolean(num1 <= num2);
		bool.setPosition(start, other.getEnd());
		bool.setContext(other.getContext());
		
		return res.success(bool);
	}
	
	public RuntimeResult greaterThan(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		if(!other.getType().equals("Number")) {
			return res.failure(new RuntimeError(
				other.getType() + "s are not comparable with Numbers.",
				other.getStart(), other.getEnd(), other.getContext()
			));
		}
		
		Double num1 = ((java.lang.Number) value).doubleValue();
		Double num2 = ((java.lang.Number) other.getValue()).doubleValue();
		
		Boolean bool = new Boolean(num1 > num2);
		bool.setPosition(start, other.getEnd());
		bool.setContext(other.getContext());
		
		return res.success(bool);
	}
	
	public RuntimeResult greaterThanOrEqualTo(Value other) {
		RuntimeResult res = new RuntimeResult();
		
		if(!other.getType().equals("Number")) {
			return res.failure(new RuntimeError(
				other.getType() + "s are not comparable with Numbers.",
				other.getStart(), other.getEnd(), other.getContext()
			));
		}
		
		Double num1 = ((java.lang.Number) value).doubleValue();
		Double num2 = ((java.lang.Number) other.getValue()).doubleValue();
		
		Boolean bool = new Boolean(num1 > num2);
		bool.setPosition(start, other.getEnd());
		bool.setContext(other.getContext());
		
		return res.success(bool);
	}
}