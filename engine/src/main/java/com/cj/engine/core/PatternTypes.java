package com.cj.engine.core;

import java.io.Serializable;

/**
 * Created by tang on 2016/3/17.
 */
public enum PatternTypes implements Serializable {
    Single(1),
    Double(2),
    Group(3),
    GSL(4);

    private int value = 0;

    PatternTypes(int value) {
        this.value = value;
    }

    public static PatternTypes getEnum(int value) {
        switch (value) {
            case 1:
                return Single;
            case 2:
                return Double;
            case 3:
                return Group;
            case 4:
                return GSL;
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