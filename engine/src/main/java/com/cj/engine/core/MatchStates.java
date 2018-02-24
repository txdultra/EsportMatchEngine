package com.cj.engine.core;

import com.cj.engine.model.CodeBaseEnum;

public enum MatchStates implements CodeBaseEnum {
    UnInitialize(0),
    Initialized(1);

    private int value = 0;

    MatchStates(int value) {
        this.value = value;
    }
    public static MatchStates getEnum(int value) {
        switch (value) {
            case 0:
                return UnInitialize;
            case 1:
                return Initialized;
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
