package com.cj.engine.dao;

import com.cj.engine.model.MatchRoundInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchRoundMapper extends CrudMapper<MatchRoundInfo,String> {
}
