package com.example.exchange.helper;

public class ResponseT<T> {
    private final boolean succeeded;
    private final T data;
    private final String errorMessage;

    public ResponseT(boolean succeeded, T data, String errorMessage) {
        this.succeeded = succeeded;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public boolean getSucceeded() {
        return succeeded;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
