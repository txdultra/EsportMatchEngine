package com.cj.engine.core;

import java.io.Serializable;

/**
 * Created by tang on 2016/3/16.
 */
public enum PlayerTypes implements Serializable {
    Enroller(1),
    Seeder(2);

    private int value = 0;

    PlayerTypes(int value) {
        this.value = value;
    }

    public static PlayerTypes getEnum(int value) {
        switch (value) {
            case 1:
                return Enroller;
            case 2:
                return Seeder;
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
