package org.motechproject.mHealthDataInterface.utility;

/**
 * Custom Exception class
 */
public class mHealthException extends Exception {
    private String message = null;

    public mHealthException() {
        super();
    }

    public mHealthException(String message) {
        super(message);
        this.message = message;
    }

    public mHealthException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
