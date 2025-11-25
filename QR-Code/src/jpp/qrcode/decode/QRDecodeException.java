package jpp.qrcode.decode;

public class QRDecodeException extends RuntimeException {

    public QRDecodeException() {
        super();
    }

    public QRDecodeException(String message) {
        super(message);
    }

    public QRDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public QRDecodeException(Throwable cause) {
        super(cause);
    }
}
