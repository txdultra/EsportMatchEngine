package com.cj.engine.dao;

import com.cj.engine.core.PatternStates;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchPatternMapper {
    PatternStates getState(String patternId);
}
