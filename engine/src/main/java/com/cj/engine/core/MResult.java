package com.cj.engine.core;

/**
 * Created by tang on 2016/3/15.
 */
public class MResult {
    public static final String SUCCESS_CODE = "0000";

    private String message;
    private String code;

    public MResult(String code,String message) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }
}

