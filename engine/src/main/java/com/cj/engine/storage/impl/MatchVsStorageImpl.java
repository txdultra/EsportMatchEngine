package com.cj.engine.storage.impl;

import com.cj.engine.cfg.caching.Cache;
import com.cj.engine.core.MatchVs;
import com.cj.engine.dao.MatchVsMapper;
import com.cj.engine.storage.IMatchVsStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Service
public class MatchVsStorageImpl implements IMatchVsStorage {

    @Autowired
    private MatchVsMapper vsMapper;

    @Autowired
    private Cache cache;

    private String cacheKey(int id) {
        return String.format("cj-match:vs:entity:%d", id);
    }

    @Override
    public void save(MatchVs vs, int matchId, int roundId) {

    }

    @Override
    public Collection<MatchVs> getVss(String groupId) {
        return vsMapper.gets(groupId);
    }

    @Override
    public MatchVs get(int id) {
        MatchVs vs = cache.get(cacheKey(id), MatchVs.class);
        if (null == vs) {
            vs = vsMapper.get(id);
            if (null != vs) {
                cache.set(cacheKey(id), vs, TimeUnit.SECONDS.convert(5, TimeUnit.DAYS));
            }
        }
        return vs;
    }
}
