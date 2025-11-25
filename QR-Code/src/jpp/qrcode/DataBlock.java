package jpp.qrcode;

public class DataBlock {
	private final byte[] dataBytes;
	private final byte[] correctionBytes;

	public DataBlock(byte[] dataBytes, byte[] correctionBytes) {
		this.dataBytes = dataBytes;
		this.correctionBytes = correctionBytes;
	}

	public byte[] dataBytes() {
		return dataBytes;
	}

	public byte[] correctionBytes() {
		return correctionBytes;
	}
}
