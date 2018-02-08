package com.cj.engine.model;

import com.cj.engine.core.VsStates;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchVsInfo extends PropertyEntity {
    private int id;
    private int leftPlayerId;
    private int rightPlayerId;
    private String leftNodeId;
    private String rightNodeId;
    private int leftScore;
    private int rightScore;
    private int winnerPlayerId;
    private String groupId;
    private VsStates state = VsStates.UnDefined;
}
