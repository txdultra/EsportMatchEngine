package com.cj.engine.service;


import com.cj.engine.core.cfg.BasePatternConfig;

import java.util.Collection;

/**
 * Created by tang on 2016/3/15.
 */
public interface ICommonService {
    BasePatternConfig getMPCfg(int matchId, int pid);
    Collection<BasePatternConfig> getMPCfgs(int matchId);
    void saveMPCfg(BasePatternConfig cfg);
    void delMPCfg(BasePatternConfig cfg);
}
