package com.cj.engine.core;

import com.cj.engine.model.CodeBaseEnum;

import java.io.Serializable;

/**
 * Created by tang on 2016/3/16.
 */
public enum PlayerTypes implements CodeBaseEnum {
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

    @Override
    public int code() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
