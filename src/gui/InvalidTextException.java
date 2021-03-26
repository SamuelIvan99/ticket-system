package gui;

public class InvalidTextException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidTextException(String message, Throwable e) {
        super(message, e);
    }
}