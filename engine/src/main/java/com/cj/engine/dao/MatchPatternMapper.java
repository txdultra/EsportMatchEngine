package com.cj.engine.dao;

import com.cj.engine.core.PatternStates;
import com.cj.engine.model.MatchPatternInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MatchPatternMapper extends CrudMapper<MatchPatternInfo,Integer> {
    List<MatchPatternInfo> gets(int matchId);

    long updateState(@Param("patternId") int patternId,@Param("state") PatternStates state);
}
