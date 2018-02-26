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

@Service
public class MatchRoundStorageImpl implements IMatchRoundStorage {

    @Autowired
    private MatchRoundMapper roundMapper;

    @Autowired
    private Cache cache;

    private String cacheKey(String id) {
        return String.format("cj-match:round_id:entity:%s", id);
    }

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
    public Collection<MatchRound> getRounds(int patternId, short category) {
        return roundMapper.gets(patternId, category);
    }

    @Override
    public boolean create(MatchRound round) {
        boolean ok = roundMapper.insert(round) > 0;
        if (ok) {
            cache.set(cacheKey(round.getId()), round, TimeUnit.SECONDS.convert(5, TimeUnit.DAYS));
        }
        return ok;
    }

    @Override
    public boolean saveOrUpdate(MatchRound round) {
        boolean ok = roundMapper.upsert(round) > 0;
        if (ok) {
            cache.del(cacheKey(round.getId()));
        }
        return ok;
    }

    @Override
    public void batchSave(Collection<MatchRound> rounds) {
        roundMapper.batchInsert(rounds);
    }

    @Override
    public void delByPatternId(int patternId) {
        Collection<String> ids = roundMapper.getIdsByPatternId(patternId);
        cache.dels(ids.stream().map(this::cacheKey).toArray(String[]::new));
        roundMapper.delByPatternId(patternId);
    }
}
