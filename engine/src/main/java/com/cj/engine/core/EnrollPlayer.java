package com.cj.engine.core;

/**
 * Created by tang on 2016/3/15.
 */
public class EnrollPlayer extends ModelBase{
    private int playerId;
    private int matchId;
    private String firstNodeId;

    //报名id
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public PlayerTypes getPlayerType() {
        return PlayerTypes.Enroller;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public void setFirstNodeId(String id) {this.firstNodeId = id;}

    public String getFirstNodeId() {return this.firstNodeId;}
}
