package com.cj.engine.storage.impl;

import com.cj.engine.cfg.caching.Cache;
import com.cj.engine.core.MatchRound;
import com.cj.engine.dao.MatchRoundMapper;
import com.cj.engine.storage.IMatchRoundStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MatchRoundStorageImpl implements IMatchRoundStorage {

    @Autowired
    private MatchRoundMapper roundMapper;

    @Autowired
    private Cache cache;

    private String cacheKey(String id) {
        return String.format("cj-match:round_id:entity:%s", id);
    }

    private String cacheKeyByPatternId(int patternId) {return  String.format("cj-match:rounds:pattern_id:%d", patternId);}

    @Override
    public MatchRound get(String id) {
        MatchRound mr = cache.get(cacheKey(id), MatchRound.class);
        if (null == mr) {
            mr = roundMapper.get(id);
            if (null != mr) {
                cache.set(cacheKey(id), mr, TimeUnit.SECONDS.convert(5, TimeUnit.DAYS));
            }
        }
        return mr;
    }

    @Override
    public Collection<MatchRound> getRounds(int patternId) {
        Collection<MatchRound> rounds = cache.getList(cacheKeyByPatternId(patternId),MatchRound.class);
        if(rounds.size() == 0) {
            rounds = roundMapper.gets(patternId);
            cache.set(cacheKeyByPatternId(patternId),rounds,TimeUnit.SECONDS.convert(5,TimeUnit.DAYS));
        }
        return rounds;
    }

    @Override
    public Collection<MatchRound> getRounds(int patternId, short category) {
        return getRounds(patternId).stream().filter(a->a.getCategory() == category).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public boolean create(MatchRound round) {
        boolean ok = roundMapper.insert(round) > 0;
        if (ok) {
            cache.set(cacheKey(round.getId()), round, TimeUnit.SECONDS.convert(5, TimeUnit.DAYS));
            cache.del(cacheKeyByPatternId(round.getPatternId()));
        }
        return ok;
    }

    @Override
    public boolean saveOrUpdate(MatchRound round) {
        boolean ok = roundMapper.upsert(round) > 0;
        if (ok) {
            cache.del(cacheKey(round.getId()));
            cache.del(cacheKeyByPatternId(round.getPatternId()));
        }
        return ok;
    }

    @Override
    public void batchSave(Collection<MatchRound> rounds) {
        if(rounds.size() > 0) {
            roundMapper.batchInsert(rounds);
            cache.del(cacheKeyByPatternId((rounds.toArray(new MatchRound[]{})[0]).getPatternId()));
        }
    }

    @Override
    public void delByPatternId(int patternId) {
        Collection<String> ids = roundMapper.getIdsByPatternId(patternId);
        cache.dels(ids.stream().map(this::cacheKey).toArray(String[]::new));
        roundMapper.delByPatternId(patternId);
    }
}
