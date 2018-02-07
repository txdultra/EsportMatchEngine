package com.cj.engine.core.cfg;


import com.cj.engine.core.PatternTypes;
import com.cj.engine.core.VerdictTypes;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/3/15.
 */
@Getter
@Setter
public abstract class MPCfg {
    private boolean initialized;
    private int id;
    private int pid;
    private int uid;
    private String title = "";
    private PatternTypes type;
    private int matchId;
    private String patternId;
    /**
     * 设置晋级人数
     */
    private int promotions;
    private VerdictTypes verdictType;
    /**
     * 设置每组人数，默认2
     */
    private int groupPlayerCount = 2;
}