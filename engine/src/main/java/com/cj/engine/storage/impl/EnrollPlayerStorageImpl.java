package com.cj.engine.storage.impl;

import com.cj.engine.cfg.caching.Cache;
import com.cj.engine.core.EnrollPlayer;
import com.cj.engine.dao.EnrollPlayerMapper;
import com.cj.engine.storage.IEnrollPlayerStorage;
import com.cj.engine.transfomers.DocTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class EnrollPlayerStorageImpl implements IEnrollPlayerStorage {

    @Autowired
    private EnrollPlayerMapper playerMapper;

    @Autowired
    private Cache cache;

    private String cacheHashKey(int matchId) {
        return String.format("cj-match:enrollers:%d", matchId);
    }

    @Override
    public Collection<EnrollPlayer> getPlayers(int matchId) {
        Collection<EnrollPlayer> players = cache.hgetall(cacheHashKey(matchId), EnrollPlayer.class);
        if (players.size() == 0) {
            players = playerMapper.gets(matchId);
            Map<String, EnrollPlayer> map = players.stream().collect(Collectors.toMap(EnrollPlayer::getPlayerId, EnrollPlayer -> EnrollPlayer));
            cache.del(cacheHashKey(matchId));
            cache.hmset(cacheHashKey(matchId), map);
        }
        return players;
    }

    @Override
    public void savePlayerFirstNode(String playerId, int matchId, String nodeId) {
        boolean ok = playerMapper.updateNode(playerId, matchId, nodeId) > 0;
        if (ok) {
            EnrollPlayer player = playerMapper.get(playerId, matchId);
            cache.hset(cacheHashKey(matchId), playerId, player);
        }
    }

    @Override
    public EnrollPlayer get(String playerId, int matchId) {
        EnrollPlayer player = cache.hget(cacheHashKey(matchId), playerId, EnrollPlayer.class);
        if (null != player) {
            player = playerMapper.get(playerId, matchId);
            if (null != player) {
                cache.hset(cacheHashKey(matchId), playerId, player);
            }
        }
        return player;
    }

    @Override
    public boolean save(EnrollPlayer player) {
        boolean ok = playerMapper.insert(player) > 0;
        if (ok) {
            cache.hset(cacheHashKey(player.getMatchId()), player.getPlayerId(), player);
        }
        return ok;
    }

    @Override
    public int counts(int matchId) {
        return playerMapper.counts(matchId);
    }
}
