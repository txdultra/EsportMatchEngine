package com.cj.engine.dao;

import com.cj.engine.model.MatchVsInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchVsMapper extends CrudMapper<MatchVsInfo,Integer> {
}
