package com.klasha.globalgeostat.core.base;

import lombok.Getter;

@Getter
public class Response<T> {
    private boolean status = true;
    private String message = "success";
    private T data;

    public Response(T data) {
        this.data = data;
    }

    public Response(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}