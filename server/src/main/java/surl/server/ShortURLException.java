package surl.server;

public class ShortURLException extends Exception {

    private static final long serialVersionUID = 1L;

    public ShortURLException(String message) {
        super(message);
    }

    public ShortURLException(String message, Throwable cause) {
        super(message, cause);
    }
}
