package com.cj.engine.core;

/**
 * Created by tang on 2016/4/7.
 */
public enum PatternItemTypes {
    Round(1),
    Group(2),
    Node(3);

    private int value = 0;

    PatternItemTypes(int value) {
        this.value = value;
    }
    public static PatternItemTypes getEnum(int value) {
        switch (value) {
            case 1:
                return Round;
            case 2:
                return Group;
            case 3:
                return Node;
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
