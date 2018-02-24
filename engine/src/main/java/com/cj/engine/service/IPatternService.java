package com.cj.engine.service;

import com.cj.engine.core.PatternStates;
import com.cj.engine.model.MatchPatternInfo;

import java.util.List;

public interface IPatternService {
    PatternStates getState(int patternId);

    boolean saveState(int patternId, PatternStates state);

    List<MatchPatternInfo> gets(int matchId);

    MatchPatternInfo get(int id);
}
