package de.uniwue.jpp.encoder;

public interface Encoder {

    public char encode (char c) throws EncoderInputException;


    public void rotate (boolean carry);


    String getType();
}
