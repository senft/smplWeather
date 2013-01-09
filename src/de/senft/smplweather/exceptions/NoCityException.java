package de.senft.smplweather.exceptions;

public class NoCityException extends Exception {

    private static final long serialVersionUID = 2956903426354607195L;

    public NoCityException(String detailMessage) {
        super(detailMessage);
    }

    public NoCityException(Throwable throwable) {
        super(throwable);
    }

    public NoCityException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}