package com.bank.callTransfer.exception;

public class LocationNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1560219599123318819L;

	public static final String LOCATION_NOT_FOUND = "Location Not Found";

	public LocationNotFoundException() {
		super(LOCATION_NOT_FOUND);
	}	
}
