package com.cj.engine.dao;

import com.cj.engine.core.MatchRound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface MatchRoundMapper extends CrudMapper<MatchRound,String> {
    Collection<MatchRound> gets(@Param("patternId") int patternId, @Param("category") short category);

    long batchInsert(@Param("list") Collection<MatchRound> list);

    void delByPatternId(@Param("patternId") int patternId);

    Collection<String> getIdsByPatternId(@Param("patternId") int patternId);

    long upsert(MatchRound round);
}
