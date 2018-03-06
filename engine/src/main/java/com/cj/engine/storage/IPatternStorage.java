package com.cj.engine.storage;

import com.cj.engine.core.PatternStates;
import com.cj.engine.model.MatchPatternInfo;

import java.util.List;

public interface IPatternStorage {
    PatternStates getState(int patternId);

    boolean saveState(int patternId, PatternStates state);

    List<MatchPatternInfo> gets(int matchId);

    MatchPatternInfo get(int id);

    boolean save(MatchPatternInfo patternInfo);
}
