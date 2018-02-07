package com.cj.engine.core;

import java.io.Serializable;

/**
 * Created by tang on 2016/3/22.
 */
public enum VsNodeState implements Serializable {
    UnDefined(0),
    Promoted(1),
    AutoPromoted(2),
    UnPromoted(3);


    private int value = 0;

    VsNodeState(int value) {
        this.value = value;
    }
    public static VsNodeState getEnum(int value) {
        switch (value) {
            case 0:
                return UnDefined;
            case 1:
                return Promoted;
            case 2:
                return AutoPromoted;
            case 3:
                return UnPromoted;
            default:
                throw new IllegalArgumentException("参数非法");
        }
    }

    public int value() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}