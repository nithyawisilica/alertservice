package com.track.alerts.exceptions;


public class TrackRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String message;

    public TrackRuntimeException(String message) {
        super(message , null,false,false);
        this.message = message;
    }

    public TrackRuntimeException(String message, Throwable throwable) {
        super(message,throwable,false,false);
        this.message = message;
    }

    public String toString() {
        return this.message;
    }

    public String getMessage() {
        return this.message;
    }
}
