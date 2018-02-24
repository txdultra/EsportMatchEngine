package com.cj.engine.core;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/3/15.
 */
@Getter
@Setter
public class MatchVs {
    private int id;
    private int leftId;
    private int rightId;
    private String leftNodeId;
    private String rightNodeId;
    private int leftScore;
    private int rightScore;
    private int winnerId;
    private String groupId;
    private VsStates state = VsStates.UnConfirm;

    public String getWinnerNodeId(){
        if(winnerId == leftId) {
            return leftNodeId;
        }
        return rightNodeId;
    }

    public int getWinnerScore() {
        if(this.leftId == winnerId) {
            return leftScore;
        }
        return rightScore;
    }

    public int getLoserScore() {
        if(this.leftId != winnerId) {
            return leftScore;
        }
        return rightScore;
    }
}
