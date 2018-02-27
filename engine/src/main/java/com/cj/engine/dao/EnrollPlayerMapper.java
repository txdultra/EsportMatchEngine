package com.cj.engine.dao;

import com.cj.engine.model.EnrollPlayerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface EnrollPlayerMapper {
    Collection<EnrollPlayerInfo> gets(int matchId);

    long updateNode(@Param("playerId") String playerId,@Param("matchId") int matchId,@Param("nodeId") String nodeId);
}
