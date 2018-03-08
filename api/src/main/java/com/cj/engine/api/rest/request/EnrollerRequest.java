package com.cj.engine.api.rest.request;

import com.cj.engine.core.PlayerTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>Create Time: 2018年03月06日</p>
 * <p>@author tangxd</p>
 **/
@Getter
@Setter
public class EnrollerRequest implements Serializable {
    @JsonProperty("player_id")
    private String playerId;
    @JsonProperty("match_id")
    private int matchId;
    private PlayerTypes type = PlayerTypes.Enroller;
    @JsonProperty("level_id")
    private int levelId;
    private Map<String,Object> properties;
}
