package com.cj.engine.api.rest.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>Create Time: 2018年03月02日</p>
 * <p>@author tangxd</p>
 **/
@Setter
@Getter
public class GroupStageRowDto implements Serializable {
    private String nodeId;
    private String groupId;
    private short wins;
    private short loses;
    private short pings;
    private double scores;
}
