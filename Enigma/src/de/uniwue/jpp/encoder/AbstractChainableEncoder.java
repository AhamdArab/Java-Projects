package de.uniwue.jpp.encoder;

public abstract class AbstractChainableEncoder implements Encoder {

	Encoder delegate;

	public AbstractChainableEncoder(Encoder delegate) throws EncoderCreationException {
		if (delegate == null) {
			throw new EncoderCreationException();
		}
		this.delegate = delegate;
	}

	public Encoder getDelegate() {
		return delegate;
	}


}