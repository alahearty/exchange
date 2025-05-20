package com.example.exchange.helper;

public class Response extends ResponseT<String> {
    public Response(Boolean succeeded, String errorMessage) {
        super(succeeded, null, errorMessage);
    }

    public static Response Fail(String errorMessage) {
        return new Response(false, errorMessage);
    }

    public static Response Success() {
        return new Response(true, null);
    }

    public static <T> ResponseT<T> FailT(String errorMessage) {
        return new ResponseT<>(false, null, errorMessage);
    }

    public static <T> ResponseT<T> SuccessT(T data) {
        return new ResponseT<>(true, data, null);
    }
}
