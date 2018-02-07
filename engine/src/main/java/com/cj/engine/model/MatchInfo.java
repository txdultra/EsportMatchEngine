package com.cj.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class MatchInfo implements Serializable{
    private int id;
    private String title;
    private Date postTime;
}
