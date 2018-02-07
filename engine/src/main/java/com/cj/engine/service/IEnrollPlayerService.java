package com.cj.engine.service;

import com.cj.engine.core.EnrollPlayer;

import java.util.Collection;

/**
 * 参赛选手
 */
public interface IEnrollPlayerService {
    Collection<EnrollPlayer> getPlayers(int matchId);

    void savePlayerFirstNode(int playerId, String nodeId);
}
