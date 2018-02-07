package com.cj.engine.service;


import com.cj.engine.core.cfg.MPCfg;

import java.util.Collection;

/**
 * Created by tang on 2016/3/15.
 */
public interface ICommonService {
    MPCfg getMPCfg(int matchId, int pid);
    Collection<MPCfg> getMPCfgs(int matchId);
    void saveMPCfg(MPCfg cfg);
    void delMPCfg(MPCfg cfg);
}
