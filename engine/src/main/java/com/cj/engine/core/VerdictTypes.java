package com.cj.engine.core;

import com.cj.engine.model.CodeBaseEnum;

import java.io.Serializable;

/**
 * 裁决模式
 * Created by tang on 2016/3/16.
 */
public enum VerdictTypes implements CodeBaseEnum {
    PLAYER(1),
    VERDICT(2);
    private int value = 0;

    VerdictTypes(int value) {
        this.value = value;
    }
    public static VerdictTypes getEnum(int value) {
        switch (value) {
            case 1:
                return PLAYER;
            case 2:
                return VERDICT;
            default:
                throw new IllegalArgumentException("参数非法");
        }
    }

    @Override
    public int code() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
