package com.cj.engine.service.impl;

import com.cj.engine.core.EnrollPlayer;
import com.cj.engine.dao.EnrollPlayerMapper;
import com.cj.engine.service.IEnrollPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EnrollPlayerServiceImpl implements IEnrollPlayerService{

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
