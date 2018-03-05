package com.cj.engine.storage;

import com.cj.engine.core.EnrollPlayer;

import java.util.Collection;

/**
 * 参赛选手
 */
public interface IEnrollPlayerStorage {
    Collection<EnrollPlayer> getPlayers(int matchId);

    void savePlayerFirstNode(String playerId, int matchId, String nodeId);

    EnrollPlayer get(String playerId, int matchId);

    boolean save(EnrollPlayer player);

}
