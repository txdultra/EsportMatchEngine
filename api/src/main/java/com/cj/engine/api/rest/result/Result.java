package com.cj.engine.api.rest.result;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * <p>Create Time: 2018年03月06日</p>
 * <p>@author tangxd</p>
 **/
public class Result implements Serializable {
    public final static String SUCCESS = "success";

    @JsonProperty("code")
    private String code;
    @JsonProperty("msg")
    private String msg;

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
