package com.cj.engine.core;

import com.cj.engine.core.cfg.BasePatternConfig;
import com.cj.engine.model.MatchPatternInfo;

import java.util.List;

/**
 * 赛制配置加载器
 */
public interface PatternConfigLoader {
    List<BasePatternConfig> gets(int matchId);

    BasePatternConfig get(int patternId);

    BasePatternConfig convert(MatchPatternInfo mpi);

}
