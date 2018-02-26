package com.cj.engine.dao;

import com.cj.engine.core.MatchStates;
import com.cj.engine.model.MatchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MatchMapper extends CrudMapper<MatchInfo, Integer> {
    long updateState(@Param("id") int id, @Param("state") MatchStates state);
}
