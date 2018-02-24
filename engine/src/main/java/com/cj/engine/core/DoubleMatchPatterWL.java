package com.cj.engine.core;

import com.cj.engine.model.CodeBaseEnum;


/**
 * Created by tang on 2016/3/17.
 */
public enum DoubleMatchPatterWL implements CodeBaseEnum {
    WINGROUP(1),
    LOSEGROUP(2);

    private int value = 0;

    DoubleMatchPatterWL(int value) {
        this.value = value;
    }
    public static DoubleMatchPatterWL getEnum(int value) {
        switch (value) {
            case 1:
                return WINGROUP;
            case 2:
                return LOSEGROUP;
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
