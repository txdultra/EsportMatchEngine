package com.cj.engine.storage;

import com.cj.engine.core.MatchVs;

import java.util.Collection;

public interface IMatchVsStorage {
    void save(MatchVs vs, int matchId, int roundId);

    Collection<MatchVs> getVss(String groupId);
}
