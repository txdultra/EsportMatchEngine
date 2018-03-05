package com.cj.matchengine.api.rest.result;

import com.cj.engine.core.PlayerTypes;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>Create Time: 2018年03月02日</p>
 * <p>@author tangxd</p>
 **/
@Getter
@Setter
public class EnrollerDto extends BaseDto {
    private String playerId;
    private int matchId;
    private String nodeId;
    private PlayerTypes type;
    private int levelId;
    private Date postTime;
}
