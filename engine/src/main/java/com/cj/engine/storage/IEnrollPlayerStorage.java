package com.cj.engine.storage;

import com.cj.engine.core.EnrollPlayer;

import java.util.Collection;

/**
 * 参赛选手
 */
public interface IEnrollPlayerStorage {
    Collection<EnrollPlayer> getPlayers(int matchId);

    void savePlayerFirstNode(int playerId, String nodeId);
}
