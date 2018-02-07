package com.cj.engine.service;

import com.cj.engine.core.MatchRound;

import java.util.Collection;

/**
 * 赛事轮次接口
 */
public interface IMatchRoundService {
    Collection<MatchRound> getRounds(String patternId);
    Collection<MatchRound> dgetRounds(String patternId);
    void save(MatchRound round);
}
