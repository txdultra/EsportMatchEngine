package com.cj.engine.model;

import com.cj.engine.core.PlayerTypes;
import com.cj.engine.core.PropertyEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollPlayerInfo extends PropertyEntity {
    private int id;
    private int matchId;
    private String nodeId;
    private PlayerTypes type = PlayerTypes.Enroller;
    private int levelId;
}
