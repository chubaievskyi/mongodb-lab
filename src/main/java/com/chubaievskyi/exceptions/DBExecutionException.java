package com.chubaievskyi.exceptions;

public class DBExecutionException extends RuntimeException {

    public DBExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}

