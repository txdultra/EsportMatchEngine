package com.cj.engine.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GroupTablesInfo {
    private String nodeId;
    private String groupId;
    private short wins;
    private short loses;
    private short pings;
    private short scores;
}
