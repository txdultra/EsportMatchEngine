package com.cj.engine.service;

import com.cj.engine.core.MatchVs;

import java.util.Collection;

public interface IMatchVsService {
    void save(MatchVs vs, int matchId, int roundId);

    Collection<MatchVs> getVss(String groupId);
}
