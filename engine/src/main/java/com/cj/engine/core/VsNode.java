package com.cj.engine.core;


/**
 * Created by tang on 2016/3/16.
 */
public class VsNode extends ModelBase{
    private String id;
    private String winNextId;
    private String loseNextId;
    private String groupId;
    private String patternId;
    private int index; //排序用
    private int playerId;
//    private EnrollPlayer player;
    private VsNodeState state;
    private boolean isInsert;
    private double score;
    private int round;
    private boolean isBye;
    private String srcNodeId;

    public VsNode(String id) {
        this.id = id;
        this.state = VsNodeState.UnDefined;
        this.winNextId = this.loseNextId = this.srcNodeId = "";
    }

    public String getId() {
        return this.id;
    }

    public String getWinNextId() {
        return winNextId;
    }

    public void setWinNextId(String winNextId) {
        this.winNextId = winNextId;
    }

    public String getLoseNextId() {
        return loseNextId;
    }

    public void setLoseNextId(String nextId) {
        this.loseNextId = nextId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPatternId() {
        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public VsNodeState getState() {
        return state;
    }

    public void setState(VsNodeState state) {
        this.state = state;
    }

    public boolean getInsert() {
        return isInsert;
    }

    public void setInsert(boolean insert) {
        isInsert = insert;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isBye() {
        return isBye;
    }

    public void setBye(boolean bye) {
        isBye = bye;
    }


    public String getSrcNodeId() {
        return srcNodeId;
    }

    public void setSrcNodeId(String srcNodeId) {
        this.srcNodeId = srcNodeId;
    }

}
