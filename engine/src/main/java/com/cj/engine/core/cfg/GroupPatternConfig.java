package com.cj.engine.core.cfg;

import com.cj.engine.core.GroupMatchPatternScoringTypes;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/4/18.
 */
public class GroupPatternConfig extends BasePatternConfig {

    private static final String SCORING_TYPE_KEY = "ScoringType";
    private static final String BIG_WIN_SCORE_KEY = "BigWinScore";
    private static final String BIG_LOSE_SCORE_KEY = "BigLoseScore";
    private static final String SMALL_WIN_SCORE_KEY = "SmallWinScore";
    private static final String SMALL_LOSE_SCORE_KEY = "SmallLoseScore";

    public GroupMatchPatternScoringTypes getScoringType() {
        if (containsKey(SCORING_TYPE_KEY)) {
            Object val = get(SCORING_TYPE_KEY);
            return GroupMatchPatternScoringTypes.getEnum(Integer.valueOf(val.toString()));
        }
        return GroupMatchPatternScoringTypes.BigSocre;
    }

    public void setScoringType(GroupMatchPatternScoringTypes type) {
        put(SCORING_TYPE_KEY, type.code());
    }

    /**
     * 赢者大分
     * @return
     */
    public double getBigWinScore() {
        if (containsKey(BIG_WIN_SCORE_KEY)) {
            return Double.parseDouble(get(BIG_WIN_SCORE_KEY).toString());
        }
        return 0d;
    }

    public void setBigWinScore(double value) {
        put(BIG_WIN_SCORE_KEY, value);
    }

    /**
     * 败者大分
     * @return
     */
    public double getBigLoseScore() {
        if (containsKey(BIG_LOSE_SCORE_KEY)) {
            return Double.parseDouble(get(BIG_LOSE_SCORE_KEY).toString());
        }
        return 0d;
    }

    public void setBigLoseScore(double value) {
        put(BIG_LOSE_SCORE_KEY, value);
    }

    /**
     * 赢者小分
     * @return
     */
    public double getSmallWinScore() {
        if (containsKey(SMALL_WIN_SCORE_KEY)) {
            return Double.parseDouble(get(SMALL_WIN_SCORE_KEY).toString());
        }
        return 0d;
    }

    public void setSmallWinScore(double value) {
        put(SMALL_WIN_SCORE_KEY, value);
    }

    /**
     * 败者小分
     * @return
     */
    public double getSmallLoseScore() {
        if (containsKey(SMALL_LOSE_SCORE_KEY)) {
            return Double.parseDouble(get(SMALL_LOSE_SCORE_KEY).toString());
        }
        return 0d;
    }

    public void setSmallLoseScore(double value) {
        put(SMALL_LOSE_SCORE_KEY, value);
    }
}
