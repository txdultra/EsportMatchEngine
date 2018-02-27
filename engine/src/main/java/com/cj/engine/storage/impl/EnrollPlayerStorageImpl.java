package com.cj.engine.storage.impl;

import com.cj.engine.core.EnrollPlayer;
import com.cj.engine.dao.EnrollPlayerMapper;
import com.cj.engine.model.EnrollPlayerInfo;
import com.cj.engine.storage.IEnrollPlayerStorage;
import com.cj.engine.transfomers.DocTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class EnrollPlayerStorageImpl implements IEnrollPlayerStorage {

    @Autowired
    private EnrollPlayerMapper playerMapper;

    @Autowired
    private DocTransformer<EnrollPlayerInfo, EnrollPlayer> transformer;

    @Override
    public Collection<EnrollPlayer> getPlayers(int matchId) {
        Collection<EnrollPlayer> players = new ArrayList<>();
        Collection<EnrollPlayerInfo> infos = playerMapper.gets(matchId);
        for (EnrollPlayerInfo info : infos) {
            players.add(transformer.transform(info));
        }
        return players;
    }

    @Override
    public void savePlayerFirstNode(String playerId, int matchId, String nodeId) {
        playerMapper.updateNode(playerId, matchId, nodeId);
    }
}
