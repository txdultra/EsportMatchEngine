package com.cj.engine.core;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/3/16.
 */
@Getter
@Setter
public class MatchRound extends BaseModel {

    private String id;
    private int patternId;
    private short round;
    private int matchId;
    private short category;
    private int groupCounts;
}
