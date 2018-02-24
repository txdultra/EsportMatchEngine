package com.cj.engine.service.impl;

import com.cj.engine.dao.MatchMapper;
import com.cj.engine.model.MatchInfo;
import com.cj.engine.service.IMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchServiceImpl implements IMatchService {

    @Autowired
    private MatchMapper matchMapper;

    @Override
    public MatchInfo get(int matchId) {
        return matchMapper.get(matchId);
    }
}
