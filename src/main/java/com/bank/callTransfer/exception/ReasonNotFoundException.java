package com.bank.callTransfer.exception;

public class ReasonNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String REASON_NOT_FOUND = "Reason Not Found";

	public ReasonNotFoundException() {
		super(REASON_NOT_FOUND);
	}	
}
