package com.cj.engine.core;

/**
 * Created by tang on 2016/3/16.
 */
public class VsGroup extends ModelBase {
    public VsGroup(String id, int groupPlayerCount){
        this.id = id;
        this.groupPlayerCount = groupPlayerCount;
    }

    private int groupPlayerCount;
    private int round;
    private String id;
    private int index;
    private String roundId;
    private String patternId;
    private int category;
    private VsStates state = VsStates.UnDefined;


    public int groupPlayerCount(){
        return this.groupPlayerCount;
    }

    public int getCurrentRound() {
        return round;
    }

    public void setCurrentRound(int currentRound) {
        this.round = currentRound;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatternId() {
        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public VsStates getState() {
        return state;
    }

    public void setState(VsStates state) {
        this.state = state;
    }
}
