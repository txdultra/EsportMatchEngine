package com.cj.engine.core;

import com.cj.engine.model.CodeBaseEnum;

import java.io.Serializable;

/**
 * Created by tang on 2016/3/16.
 */
public enum PatternStates implements CodeBaseEnum {
    UnBuildSchedule(1),
    BuildedSchedule(2),
    AssignedPlayers(3),
    EstablishedVs(4),
    FINISHED(99);

    private int value = 0;

    PatternStates(int value) {
        this.value = value;
    }
    public static PatternStates getEnum(int value) {
        switch (value) {
            case 1:
                return UnBuildSchedule;
            case 2:
                return BuildedSchedule;
            case 3:
                return AssignedPlayers;
            case 4:
                return EstablishedVs;
            case 99:
                return FINISHED;
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
