package ru.msmai.a1.exceptions;

public class MsmConsistencyException extends MsmException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6973829419065727769L;

	public MsmConsistencyException() {
	}

	public MsmConsistencyException(String message) {
		super(message);
	}

	public MsmConsistencyException(Throwable cause) {
		super(cause);
	}

	public MsmConsistencyException(String message, Throwable cause) {
		super(message, cause);
	}

	public MsmConsistencyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
