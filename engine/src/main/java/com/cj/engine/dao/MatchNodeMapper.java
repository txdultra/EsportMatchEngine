package com.cj.engine.dao;

import com.cj.engine.core.VsNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface MatchNodeMapper extends CrudMapper<VsNode,String> {
    Collection<VsNode> gets(int patternId);

    void batchInsert(@Param("list") Collection<VsNode> list);

    long upsert(VsNode node);

    void delByPatternId(@Param("patternId") int patternId);

    Collection<String> getIdsByPatternId(@Param("patternId") int patternId);
}
