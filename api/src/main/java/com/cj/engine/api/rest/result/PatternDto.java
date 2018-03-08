package com.cj.engine.api.rest.result;

import com.cj.engine.core.PatternStates;
import com.cj.engine.core.PatternTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * <p>Create Time: 2018年03月01日</p>
 * <p>@author tangxd</p>
 **/
@Setter
@Getter
public class PatternDto extends BaseDto {
    @JsonProperty("id")
    private int id;
    @JsonProperty("match_id")
    private int matchId;
    @JsonProperty("type")
    private PatternTypes type;
    @JsonProperty("bo")
    private int bo;
    @JsonProperty("state")
    private PatternStates state;
    @JsonProperty("index")
    private int index;
    @JsonProperty("title")
    private String title;
    @JsonProperty("promotions")
    private int promotions;
    @JsonProperty("group_players")
    private int groupPlayers;
    @JsonProperty("post_time")
    private Date postTime = new Date();
    @JsonProperty("rounds")
    private Collection<RoundDto> rounds = new ArrayList<>();
}
