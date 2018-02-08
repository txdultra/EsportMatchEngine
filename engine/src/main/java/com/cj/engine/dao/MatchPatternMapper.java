package com.cj.engine.dao;

import com.cj.engine.model.MatchPatternInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchPatternMapper extends CrudMapper<MatchPatternInfo,Integer> {

}
