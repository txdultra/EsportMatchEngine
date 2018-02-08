package com.cj.engine.model;

import com.cj.engine.core.VsStates;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class MatchGroupInfo extends PropertyEntity {
    private String id;
    private int players;
    private int patternId;
    private short index;
    private short winners;
    private short rounds;
    private String roundId;
    private short category;
    private VsStates state = VsStates.UnDefined;
    private Date postTime;
}
