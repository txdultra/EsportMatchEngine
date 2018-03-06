package com.cj.engine.core;

import com.cj.engine.model.CodeBaseEnum;

public enum MatchStates implements CodeBaseEnum {
    UnInitialize(0),
    Initialized(1),
    Assigned(2),
    Finished(99);

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
            case 2:
                return Assigned;
            case 99:
                return Finished;
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
