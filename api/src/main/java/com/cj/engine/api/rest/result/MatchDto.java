package com.cj.engine.api.rest.result;

import com.cj.engine.core.MatchStates;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Create Time: 2018年03月01日</p>
 * <p>@author tangxd</p>
 **/
@Getter
@Setter
public class MatchDto extends BaseDto {
    @JsonProperty("id")
    private int id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("state")
    private MatchStates state;
    @JsonProperty("pattern_ids")
    private Collection<Integer> patternIds = new ArrayList<>();
    @JsonProperty("patterns")
    private Collection<PatternDto> patterns;
}