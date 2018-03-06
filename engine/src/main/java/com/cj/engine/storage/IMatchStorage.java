package com.cj.engine.storage;

import com.cj.engine.core.MatchStates;
import com.cj.engine.model.MatchInfo;

public interface IMatchStorage {
    MatchInfo get(int matchId);

    boolean saveState(int matchId, MatchStates state);

    boolean save(MatchInfo match);
}
