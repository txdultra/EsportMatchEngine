package com.cj.engine.core.cfg;

import com.cj.engine.core.GroupMatchPatternScoringTypes;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/4/18.
 */
@Getter
@Setter
public class GroupMPCfg extends MPCfg {

    private GroupMatchPatternScoringTypes scoringType = GroupMatchPatternScoringTypes.BigSocre;
    private double bWinScore;
    private double bLoseScore;
    private double sWinScore;
    private double sLoseScore;
}
