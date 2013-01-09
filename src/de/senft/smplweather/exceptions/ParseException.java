package de.senft.smplweather.exceptions;

public class ParseException extends Exception {

    private static final long serialVersionUID = 2240169330911671029L;

    public ParseException(String detailMessage) {
        super(detailMessage);
    }

    public ParseException(Throwable throwable) {
        super(throwable);
    }

    public ParseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}