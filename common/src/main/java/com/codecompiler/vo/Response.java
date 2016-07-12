package com.codecompiler.vo;

import java.util.Date;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
public class Response<T> {
    private final Date timestamp;
    private final T data;

    public Response(T data) {
        this.timestamp = new Date();
        this.data = data;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public static Response createEmptyResponse() {
        return new Response(null);
    }
}
