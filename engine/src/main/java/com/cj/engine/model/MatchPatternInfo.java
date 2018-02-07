package com.cj.engine.model;

import com.cj.engine.core.PatternStates;
import com.cj.engine.core.PatternTypes;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class MatchPatternInfo implements Serializable {
    private int id;
    private int matchId;
    private String patternCfg;
    private PatternTypes patternType;
    private PatternStates patternState;
    private short index;
    private int pid;
    private String title;
    private int promotions;
    private int groupPlayers;
    private Date postTime;
}
