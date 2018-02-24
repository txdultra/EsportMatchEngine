package com.cj.engine.dao;

import com.cj.engine.core.MatchVs;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface MatchVsMapper extends CrudMapper<MatchVs,Integer> {
    Collection<MatchVs> gets(String groupId);
}
