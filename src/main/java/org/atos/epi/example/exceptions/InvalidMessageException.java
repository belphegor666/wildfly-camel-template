package org.atos.epi.example.exceptions;

public class InvalidMessageException extends Exception {
	
	private static final long serialVersionUID = -1451537157408814718L;

	public InvalidMessageException () {
		super();
	}
	
	public InvalidMessageException (String message) {
		super(message);
	}

	public InvalidMessageException (Throwable cause) {
		super(cause);
	}
	
	public InvalidMessageException (String message, Throwable cause) {
		super(message, cause);
	}
}
