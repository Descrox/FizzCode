package com.fizz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.fizz.interpreter.Interpreter;
import com.fizz.interpreter.RuntimeResult;
import com.fizz.lexer.Lexer;
import com.fizz.parser.ParseResult;
import com.fizz.parser.Parser;
import com.fizz.token.Token;
import com.fizz.util.Constants;
import com.fizz.util.error.Error;

public class Launcher {
	public static void main(String[] args) {
		Constants.init();
		final String SHELL = "<shell>";
		InputStreamReader isReader = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(isReader);
		String input = "";
		
		try {
			while(true) {
				System.out.print("Fizz > ");
				input = reader.readLine();
				if(input.equals("exit")) {return;}
				if(input.equals("")) {continue;}
				
				System.out.println(run(SHELL, input));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static String run(String fName, String fText) {
		Lexer lexer = new Lexer(fName, fText);
		Object lexResult = lexer.createTokenArray();
		
		if(lexResult instanceof Error) {
			return lexResult.toString();
		}
		
		Parser.init((ArrayList<Token>) lexResult);
		ParseResult parseResult = Parser.parse();
		
		if(parseResult.getError() != null) {
			return parseResult.getError().toString();
		}
		
		RuntimeResult interResult = Interpreter.visit(parseResult.getResult(), Constants.PROGRAM);
		if(interResult.getError() != null) {
			return interResult.getError().toString();
		}
		
		return interResult.getResult().toString();
	}
}