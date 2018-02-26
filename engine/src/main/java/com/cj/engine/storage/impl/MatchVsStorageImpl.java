package com.cj.engine.storage.impl;

import com.cj.engine.core.MatchVs;
import com.cj.engine.dao.MatchVsMapper;
import com.cj.engine.storage.IMatchVsStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MatchVsStorageImpl implements IMatchVsStorage {

    @Autowired
    private MatchVsMapper vsMapper;

    @Override
    public void save(MatchVs vs, int matchId, int roundId) {

    }

    @Override
    public Collection<MatchVs> getVss(String groupId) {
        return vsMapper.gets(groupId);
    }
}
