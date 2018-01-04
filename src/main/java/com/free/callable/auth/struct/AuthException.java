package com.free.callable.auth.struct;

/**
 * Created by  on 2016/9/19.
 */
public class AuthException extends Exception {
    private static final long serialVersionUID = 4529606885256651455L;

    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
