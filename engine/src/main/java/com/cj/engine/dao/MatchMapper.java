package com.cj.engine.dao;

import com.cj.engine.model.MatchInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchMapper extends CrudMapper<MatchInfo,Integer> {
}
