package com.cj.engine.dao;

import com.cj.engine.core.VsGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface MatchGroupMapper extends CrudMapper<VsGroup,String> {
    Collection<VsGroup> gets(@Param("patternId") int patternId,@Param("category") short category);
}
