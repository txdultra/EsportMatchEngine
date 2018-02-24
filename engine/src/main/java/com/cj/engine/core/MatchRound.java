package com.cj.engine.core;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/3/16.
 */
@Getter
@Setter
public class MatchRound extends ModelBase{

    public MatchRound() {
        this.modify();
    }

    private String id;
    private int patternId;
    private int round;
    private int matchId;
    private int category;
    private int groupCounts;
}
