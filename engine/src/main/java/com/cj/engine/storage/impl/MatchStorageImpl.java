package com.cj.engine.storage.impl;

import com.cj.engine.core.MatchStates;
import com.cj.engine.dao.MatchMapper;
import com.cj.engine.model.MatchInfo;
import com.cj.engine.storage.IMatchStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchStorageImpl implements IMatchStorage {

    @Autowired
    private MatchMapper matchMapper;

    @Override
    public MatchInfo get(int matchId) {
        return matchMapper.get(matchId);
    }

    @Override
    public boolean saveState(int matchId, MatchStates state) {
        return matchMapper.updateState(matchId, state) > 0;
    }

    @Override
    public boolean save(MatchInfo match) {
        return matchMapper.insert(match) > 0;
    }
}
