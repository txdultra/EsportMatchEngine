package com.cj.engine.dao;

import com.cj.engine.core.GroupStageRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface MatchGroupStageMapper extends CrudMapper<GroupStageRow,String> {

    long delByGroupId(@Param("groupId") String groupId);

    Collection<String> getNodeIdsByGroupId(@Param("groupId") String groupId);

    long upsert(GroupStageRow row);

    Collection<GroupStageRow> gets(@Param("groupId") String groupId);
}
