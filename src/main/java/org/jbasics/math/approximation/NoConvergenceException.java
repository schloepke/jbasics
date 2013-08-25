package org.jbasics.math.approximation;

public class NoConvergenceException extends RuntimeException {

	public NoConvergenceException() {
		super();
	}

	public NoConvergenceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NoConvergenceException(final String message) {
		super(message);
	}

	public NoConvergenceException(final Throwable cause) {
		super(cause);
	}

}
