package com.cj.engine.api.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>Create Time: 2018年03月06日</p>
 * <p>@author tangxd</p>
 **/
@Getter
@Setter
public class PreviewScheduleRequest implements Serializable {
    @JsonProperty("patterns")
    private Collection<PatternRequest> patterns;
    @JsonProperty("max_players")
    private int maxPlayers;
}
