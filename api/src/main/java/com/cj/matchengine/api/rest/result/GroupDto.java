package com.cj.matchengine.api.rest.result;

import com.cj.engine.core.VsStates;
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
@Getter
@Setter
public class GroupDto extends BaseDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("players")
    private int players;
    @JsonProperty("pattern_id")
    private int patternId;
    @JsonProperty("match_id")
    private int matchId;
    @JsonProperty("index")
    private int index;
    @JsonProperty("winners")
    private int winners;
    @JsonProperty("round")
    private int round;
    @JsonProperty("round_id")
    private String roundId;
    @JsonProperty("category")
    private short category;
    @JsonProperty("state")
    private VsStates state;
    @JsonProperty("post_time")
    private Date postTime;
    @JsonProperty("node_vss")
    private Collection<NodeDto> nodeVss= new ArrayList<>();
    @JsonProperty("vss")
    private Collection<VsDto> vss = new ArrayList<>();
    @JsonProperty("stage_rows")
    private Collection<GroupStageRowDto> stageRows = new ArrayList<>();
}
