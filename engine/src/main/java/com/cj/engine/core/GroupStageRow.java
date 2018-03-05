package com.cj.engine.core;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/4/13.
 */
@Getter
@Setter
public class GroupStageRow extends BaseModel {

    private String nodeId;
    private String groupId;
    private short wins;
    private short loses;
    private short pings;
    private double scores;

    public void addWins(int ws) {
        this.wins += ws;
    }

    public void addLoses(int ls) {
        this.loses += ls;
    }

    public void addPings(int pings) {
        this.pings += pings;
    }

    private void addScore(double score) {
        this.scores += score;
    }
}
