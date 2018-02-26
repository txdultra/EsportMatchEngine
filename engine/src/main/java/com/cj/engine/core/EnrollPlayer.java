package com.cj.engine.core;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/3/15.
 */
@Getter
@Setter
public class EnrollPlayer extends BaseModel {
    private int playerId;
    private int matchId;
    private String firstNodeId;

    public PlayerTypes getPlayerType() {
        return PlayerTypes.Enroller;
    }
}
