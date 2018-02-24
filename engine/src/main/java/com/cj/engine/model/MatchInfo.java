package com.cj.engine.model;

import com.cj.engine.core.MatchStates;
import com.cj.engine.core.PropertyEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MatchInfo extends PropertyEntity {
    private int id;
    private String title;
    private MatchStates state = MatchStates.UnInitialize;
    private Date postTime;
}
