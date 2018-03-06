package com.cj.engine.dao;

import com.cj.engine.core.EnrollPlayer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface EnrollPlayerMapper {
    Collection<EnrollPlayer> gets(int matchId);

    EnrollPlayer get(@Param("playerId") String playerId, @Param("matchId") int matchId);

    long updateNode(@Param("playerId") String playerId, @Param("matchId") int matchId, @Param("nodeId") String nodeId);

    long insert(EnrollPlayer player);

    long update(EnrollPlayer player);

    int counts(@Param("matchId") int matchId);
}
