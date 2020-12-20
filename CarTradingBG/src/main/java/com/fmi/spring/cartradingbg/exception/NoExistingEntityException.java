package com.fmi.spring.cartradingbg.exception;

public class NoExistingEntityException extends RuntimeException {
    public NoExistingEntityException() {
    }

    public NoExistingEntityException(String message) {
        super(message);
    }

    public NoExistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoExistingEntityException(Throwable cause) {
        super(cause);
    }
}
