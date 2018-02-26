package com.cj.engine.dao;

import com.cj.engine.core.VsGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface MatchGroupMapper extends CrudMapper<VsGroup,String> {
    Collection<VsGroup> gets(@Param("patternId") int patternId,@Param("category") short category);

    long batchInsert(@Param("list") Collection<VsGroup> list);

    long upsert(VsGroup group);

    void delByPatternId(@Param("patternId") int patternId);

    Collection<String> getIdsByPatternId(@Param("patternId") int patternId);
}
