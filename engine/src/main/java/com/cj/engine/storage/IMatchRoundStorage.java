package com.cj.engine.storage;

import com.cj.engine.core.MatchRound;

import java.util.Collection;

/**
 * 赛事轮次接口
 */
public interface IMatchRoundStorage {
    MatchRound get(String id);

    Collection<MatchRound> getRounds(int patternId, short category);

    boolean create(MatchRound round);

    boolean saveOrUpdate(MatchRound round);

    void batchSave(Collection<MatchRound> rounds);

    void delByPatternId(int patternId);
}
