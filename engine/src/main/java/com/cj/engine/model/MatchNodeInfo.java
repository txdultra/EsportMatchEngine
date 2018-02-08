package com.cj.engine.model;

import com.cj.engine.core.VsNodeState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchNodeInfo extends PropertyEntity {
    private String id;
    private String winNextId;
    private String loseNextId;
    private String groupId;
    private int patternId;
    private short index;
    private int playerId;
    private VsNodeState state = VsNodeState.UnDefined;
    private int score;
    private short rounds;
    private boolean isEmpty;
    private String pid;
}
