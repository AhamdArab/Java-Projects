package jpp.qrcode;

public class QRCodeException extends RuntimeException {

    public QRCodeException() {
        super();
    }

    public QRCodeException(String message) {
        super(message);
    }

    public QRCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public QRCodeException(Throwable cause) {
        super(cause);
    }

    protected QRCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
