package com.cj.engine.core;

/**
 * Created by tang on 2016/3/15.
 */
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
    private int aImg;
    private int bImg;
    private int verdictUid;
    private int verdictTime;

    public int getId() {
        return id;
    }

    public void setId(int leftId) {
        this.id = id;
    }

    public int getLeftId() {
        return leftId;
    }

    public void setLeftId(int leftId) {
        this.leftId = leftId;
    }

    public int getRightId() {
        return rightId;
    }

    public void setRightId(int rightId) {
        this.rightId = rightId;
    }

    public int getLeftScore() {
        return leftScore;
    }

    public void setLeftScore(int leftScore) {
        this.leftScore = leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }

    public void setRightScore(int rightScore) {
        this.rightScore = rightScore;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public String getWinnerNodeId(){
        if(winnerId == leftId)
            return leftNodeId;
        return rightNodeId;
    }

    public int getWinnerScore() {
        if(this.leftId == winnerId)
            return leftScore;
        return rightScore;
    }

    public int getLoserScore() {
        if(this.leftId != winnerId)
            return leftScore;
        return rightScore;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public VsStates getState() {
        return state;
    }

    public void setState(VsStates state) {
        this.state = state;
    }

    public String getLeftNodeId() {
        return leftNodeId;
    }

    public void setLeftNodeId(String leftNodeId) {
        this.leftNodeId = leftNodeId;
    }

    public String getRightNodeId() {
        return rightNodeId;
    }

    public void setRightNodeId(String rightNodeId) {
        this.rightNodeId = rightNodeId;
    }

    public int getaImg() {
        return aImg;
    }

    public void setaImg(int aImg) {
        this.aImg = aImg;
    }

    public int getbImg() {
        return bImg;
    }

    public void setbImg(int bImg) {
        this.bImg = bImg;
    }

    public int getVerdictUid() {
        return verdictUid;
    }

    public void setVerdictUid(int verdictUid) {
        this.verdictUid = verdictUid;
    }

    public int getVerdictTime() {
        return verdictTime;
    }

    public void setVerdictTime(int verdictTime) {
        this.verdictTime = verdictTime;
    }
}
