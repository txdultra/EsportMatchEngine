package com.cj.engine.core;

/**
 * Created by tang on 2016/3/29.
 */
public enum GroupMatchPatternScoringTypes {
    BigSocre(1),
    SmallScore(2),
    Mixture(3);

    private int value = 0;

    GroupMatchPatternScoringTypes(int value) {
        this.value = value;
    }
    public static GroupMatchPatternScoringTypes getEnum(int value) {
        switch (value) {
            case 1:
                return BigSocre;
            case 2:
                return SmallScore;
            case 3:
                return Mixture;
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
