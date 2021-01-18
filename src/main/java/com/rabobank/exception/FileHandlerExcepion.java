package com.rabobank.exception;

public class FileHandlerExcepion extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileHandlerExcepion(String message){
		super(message);
	}
	
	public FileHandlerExcepion(String message, Exception ex){
		super(message, ex);
	}

}
