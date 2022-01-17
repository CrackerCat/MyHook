package org.xk.xpexample.utils;

public class ReflectException extends RuntimeException {
    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}
