package com.cj.engine.model;

import com.cj.engine.core.PlayerTypes;
import com.cj.engine.core.PropertyEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EnrollPlayerInfo extends PropertyEntity {
    private String playerId;
    private int matchId;
    private String nodeId;
    private PlayerTypes type = PlayerTypes.Enroller;
    private int levelId;
    private Date postTime;

}
