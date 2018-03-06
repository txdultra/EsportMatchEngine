package com.cj.engine.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by tang on 2016/3/15.
 */
@Getter
@Setter
public class EnrollPlayer extends BaseModel {
    private String playerId;
    private int matchId;
    private String nodeId;
    private PlayerTypes type = PlayerTypes.Enroller;
    private int levelId;
    private Date postTime = new Date();
}
