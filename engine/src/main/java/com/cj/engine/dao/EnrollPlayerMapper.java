package com.cj.engine.dao;

import com.cj.engine.core.EnrollPlayer;
import com.cj.engine.model.EnrollPlayerInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface EnrollPlayerMapper extends CrudMapper<EnrollPlayerInfo, Integer> {
    Collection<EnrollPlayer> gets(int matchId);
}
