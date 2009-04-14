package fscode.exception;

import fscode.Emitter;

/**
 * Just an error message to display to the user when something happens that
 * shouldn't have happened.
 *
 * @author cmiller
 * @since 0.1
 */
public class NonfatalException {

	private String message;

	private Emitter source;

	public NonfatalException(Emitter source, String message) {
		this.source = source;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Emitter getSource() {
		return source;
	}

}
