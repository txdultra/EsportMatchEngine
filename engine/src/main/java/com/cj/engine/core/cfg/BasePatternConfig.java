package com.cj.engine.core.cfg;


import com.cj.engine.core.PatternTypes;
import com.cj.engine.core.VerdictTypes;
import lombok.Getter;
import lombok.Setter;

import java.util.AbstractMap;
/**
 * Created by tang on 2016/3/15.
 */
@Getter
@Setter
public abstract class BasePatternConfig extends AbstractMap<String,Object> {
    private int id;
    private int pid;
    private PatternTypes type;
    private int matchId;
    private String patternId;
    private int index;
    /**
     * 设置晋级人数
     */
    private int promotions;
    private VerdictTypes verdictType;
    private int groupPlayerNumber = 2;
}