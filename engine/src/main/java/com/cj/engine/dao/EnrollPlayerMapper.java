package com.cj.engine.dao;

import com.cj.engine.model.EnrollPlayerInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnrollPlayerMapper extends CrudMapper<EnrollPlayerInfo, Integer> {
}
