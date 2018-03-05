package com.cj.matchengine.api.rest.result;

import com.cj.engine.core.VsStates;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Create Time: 2018年03月02日</p>
 * <p>@author tangxd</p>
 **/
@Getter
@Setter
public class VsDto extends BaseDto {
    @JsonProperty("id")
    private int id;
    @JsonProperty("left_player_id")
    private String leftPlayerId;
    @JsonProperty("left_player")
    private EnrollerDto leftPlayer;
    @JsonProperty("right_player_id")
    private String rightPlayerId;
    @JsonProperty("right_player")
    private EnrollerDto rightPlayer;
    @JsonProperty("left_node_id")
    private String leftNodeId;
    @JsonProperty("right_node_id")
    private String rightNodeId;
    @JsonProperty("left_score")
    private int leftScore;
    @JsonProperty("right_score")
    private int rightScore;
    @JsonProperty("winner_player_id")
    private String winnerPlayerId;
    @JsonProperty("winner_player")
    private EnrollerDto winnerPlayer;
    @JsonProperty("group_id")
    private String groupId;
    @JsonProperty("match_id")
    private int matchId;
    @JsonProperty("state")
    private VsStates state;
}
