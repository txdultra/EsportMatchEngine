package com.cj.engine.service;

import com.cj.engine.core.MatchRound;

import java.util.Collection;
import java.util.List;

/**
 * 赛事轮次接口
 */
public interface IMatchRoundService {
    MatchRound get(String id);

    Collection<MatchRound> getRounds(int patternId, short category);

    boolean save(MatchRound round);

    void batchSave(Collection<MatchRound> rounds);
}
