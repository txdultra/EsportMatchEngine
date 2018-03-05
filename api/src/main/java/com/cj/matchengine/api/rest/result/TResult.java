package com.cj.matchengine.api.rest.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>Create Time: 2018年03月01日</p>
 * <p>@author tangxd</p>
 **/
@Getter
@Setter
public class TResult<T> implements Serializable {

    public final static String SUCCESS = "success";

    @JsonProperty("code")
    private String code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private T data;

    public TResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
