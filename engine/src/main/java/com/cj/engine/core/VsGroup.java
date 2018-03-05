package com.cj.engine.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by tang on 2016/3/16.
 */
@Setter
@Getter
public class VsGroup extends BaseModel {
    private String id;
    private int groupPlayerCount;
    private int patternId;
    private int matchId;
    private int index;
    private short winners;
    private int round;
    private String roundId;
    private short category;
    private VsStates state = VsStates.UnDefined;
    private Date postTime = new Date();
}
