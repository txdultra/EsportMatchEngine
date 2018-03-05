package com.cj.matchengine.api.rest.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Create Time: 2018年03月01日</p>
 * <p>@author tangxd</p>
 **/
@Setter
@Getter
public class RoundDto extends BaseDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("category")
    private short category;
    @JsonProperty("pattern_id")
    private int patternId;
    @JsonProperty("match_id")
    private int matchId;
    @JsonProperty("round")
    private short round;
    @JsonProperty("group_counts")
    private short groupCounts;
    @JsonProperty("groups")
    private Collection<GroupDto> groups = new ArrayList<>();
}
