package com.cj.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MatchRoundInfo extends PropertyEntity {
    private int id;
    private short category;
    private int patternId;
    private int matchId;
    private short rounds;
    private short groups;
}
