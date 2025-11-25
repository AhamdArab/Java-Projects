package de.uniwue.jpp.encoder;

public class EncoderCreationException extends Exception {

	public EncoderCreationException () {
		super();
	}
	
	public EncoderCreationException(String s) {
		super(s);
	}
	
	public EncoderCreationException(String s, Throwable cause) {
		super(s, cause);
	}
}
