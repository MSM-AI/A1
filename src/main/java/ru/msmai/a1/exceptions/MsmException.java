package ru.msmai.a1.exceptions;

public class MsmException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6973829419065727769L;

	public MsmException() {
	}

	public MsmException(String message) {
		super(message);
	}

	public MsmException(Throwable cause) {
		super(cause);
	}

	public MsmException(String message, Throwable cause) {
		super(message, cause);
	}

	public MsmException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
