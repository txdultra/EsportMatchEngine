package com.cj.engine.core;

/**
 * Created by tang on 2016/4/5.
 */
public enum VsStates {
    UnDefined(0),
    UnConfirm(1),
    Confirmed(2);

    private int value = 0;

    VsStates(int value) {
        this.value = value;
    }
    public static VsStates getEnum(int value) {
        switch (value) {
            case 0:
                return UnDefined;
            case 1:
                return UnConfirm;
            case 2:
                return Confirmed;
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
