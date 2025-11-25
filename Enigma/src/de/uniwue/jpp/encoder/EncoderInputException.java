package de.uniwue.jpp.encoder;

public class EncoderInputException extends Exception {


	public EncoderInputException () {
		super();
	}
	
	public EncoderInputException(String s) {
		super(s);
	}
	
	public EncoderInputException(String s, Throwable cause) {
		super(s, cause);
	}
}