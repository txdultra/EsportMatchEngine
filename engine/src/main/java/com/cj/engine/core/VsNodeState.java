package com.cj.engine.core;

import com.cj.engine.model.CodeBaseEnum;


/**
 * Created by tang on 2016/3/22.
 */
public enum VsNodeState implements CodeBaseEnum {
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

    @Override
    public int code() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}