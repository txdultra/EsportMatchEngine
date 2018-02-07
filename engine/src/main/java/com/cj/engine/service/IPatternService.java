package com.cj.engine.service;

import com.cj.engine.core.PatternStates;

public interface IPatternService {
    PatternStates getState(String patternId);

    void saveState(String patternId, PatternStates state);
}
