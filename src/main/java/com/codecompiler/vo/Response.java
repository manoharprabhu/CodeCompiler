package com.codecompiler.vo;

import java.util.Date;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
public class Response<T> {
    private final int responseCode;
    private final Date timestamp;
    private final T data;

    public Response(int responseCode, Date timestamp, T data) {
        this.responseCode = responseCode;
        this.timestamp = timestamp;
        this.data = data;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public static Response createEmptyResponse() {
        return new Response(200, new Date(), null);
    }
}
