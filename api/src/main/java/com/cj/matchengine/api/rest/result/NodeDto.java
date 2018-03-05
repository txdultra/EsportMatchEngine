package com.cj.matchengine.api.rest.result;

import com.cj.engine.core.VsNodeState;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Create Time: 2018年03月01日</p>
 * <p>@author tangxd</p>
 **/
@Getter
@Setter
public class NodeDto extends BaseDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("winner_next_id")
    private String winnerNextId;
    @JsonProperty("lose_next_id")
    private String loseNextId;
    @JsonProperty("group_id")
    private String groupId;
    @JsonProperty("pattern_id")
    private int patternId;
    @JsonProperty("match_id")
    private int matchId;
    @JsonProperty("index")
    private short index;
    @JsonProperty("player_id")
    private String playerId;
    @JsonProperty("player")
    private EnrollerDto player;
    @JsonProperty("state")
    private VsNodeState state;
    @JsonProperty("score")
    private int score;
    @JsonProperty("round")
    private short round;
    @JsonProperty("empty")
    private boolean empty;
    @JsonProperty("parent_id")
    private String parentId;
}
