package com.cj.engine.dao;

import com.cj.engine.model.MatchGroupInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchGroupMapper extends CrudMapper<MatchGroupInfo,String> {

}
