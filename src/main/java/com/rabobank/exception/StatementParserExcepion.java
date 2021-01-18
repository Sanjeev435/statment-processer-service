package com.rabobank.exception;

public class StatementParserExcepion extends RuntimeException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StatementParserExcepion(String message){
		super(message);
	}
	
	public StatementParserExcepion(String message, Exception ex){
		super(message, ex);
	}

}
