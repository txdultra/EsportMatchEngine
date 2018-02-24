package com.cj.engine.dao;

import com.cj.engine.core.VsNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface MatchNodeMapper extends CrudMapper<VsNode,String> {
    Collection<VsNode> gets(int patternId);

    void batchInsert(List<VsNode> nodes);
}
