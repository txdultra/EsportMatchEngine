package com.cj.engine.core;


import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/3/16.
 */
@Getter
@Setter
public class VsNode extends BaseModel {
    private String id;
    private String winNextId = "";
    private String loseNextId = "";
    private String groupId;
    private int patternId;
    private int matchId;
    private short index;
    private String playerId = "";
    private VsNodeState state =VsNodeState.UnDefined;
    private boolean isInsert;
    private int score;
    private short round;
    private boolean empty;
    private String srcNodeId = "";
}
