package com.cj.engine.dao;

import com.cj.engine.model.GroupTablesInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchGroupTableMapper extends CrudMapper<GroupTablesInfo,String> {
}
