package com.cj.engine.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by tang on 2016/3/15.
 */
@Getter
@Setter
public class MatchVs {
    private int id;
    private String leftId = "";
    private String rightId = "";
    private String leftNodeId;
    private String rightNodeId;
    private int leftScore;
    private int rightScore;
    private String winnerId= "";
    private String groupId;
    private int matchId;
    private VsStates state = VsStates.UnConfirm;

    public String getWinnerNodeId(){
        if(winnerId.equals(leftId)) {
            return leftNodeId;
        }
        return rightNodeId;
    }

    public int getWinnerScore() {
        if(this.leftId.equals(winnerId)) {
            return leftScore;
        }
        return rightScore;
    }

    public int getLoserScore() {
        if(!this.leftId.equals(winnerId)) {
            return leftScore;
        }
        return rightScore;
    }
}
