package com.cj.engine.dao;

import com.cj.engine.model.MatchNodeInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchNodeMapper extends CrudMapper<MatchNodeInfo,String> {
}
