package com.cj.engine.api.rest.request;

import com.cj.engine.core.PatternTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>Create Time: 2018年03月06日</p>
 * <p>@author tangxd</p>
 **/
@Setter
@Getter
public class PatternRequest implements Serializable {
    @JsonProperty("match_id")
    private int matchId;
    private PatternTypes type;
    private short bo;
    private short index;
    private String title;
    private int promotions;
    @JsonProperty("group_players")
    private short groupPlayers;
    private Map<String,Object> properties = new TreeMap<>();
}
