package com.cj.engine.storage.impl;

import com.cj.engine.core.EnrollPlayer;
import com.cj.engine.dao.EnrollPlayerMapper;
import com.cj.engine.storage.IEnrollPlayerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EnrollPlayerStorageImpl implements IEnrollPlayerStorage {

    @Autowired
    private EnrollPlayerMapper playerMapper;

    @Override
    public Collection<EnrollPlayer> getPlayers(int matchId) {
        return playerMapper.gets(matchId);
    }

    @Override
    public void savePlayerFirstNode(int playerId, String nodeId) {

    }
}
