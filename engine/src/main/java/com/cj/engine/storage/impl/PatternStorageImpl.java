package com.cj.engine.storage.impl;

import com.cj.engine.cfg.caching.Cache;
import com.cj.engine.core.PatternStates;
import com.cj.engine.dao.MatchPatternMapper;
import com.cj.engine.model.MatchPatternInfo;
import com.cj.engine.storage.IPatternStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PatternStorageImpl implements IPatternStorage {
    @Autowired
    private MatchPatternMapper patternMapper;

    @Autowired
    private Cache cache;

    private String cacheKey(int id) {
        return String.format("cj-match:pattern:%d", id);
    }

    private String cacheKeyOnList(int matchId) {
        return String.format("cj-match:patterns_by_match_id:%d", matchId);
    }

    @Override
    public PatternStates getState(int patternId) {
        MatchPatternInfo mpi = get(patternId);
        if (null == mpi) {
            return null;
        }
        return mpi.getState();
    }

    @Override
    public boolean saveState(int patternId, PatternStates state) {
        MatchPatternInfo mpi = get(patternId);
        if(patternMapper.updateState(patternId,state) > 0) {
            cache.del(cacheKey(patternId));
            if(null != mpi) {
                cache.del(cacheKeyOnList(mpi.getMatchId()));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<MatchPatternInfo> gets(int matchId) {
        List<MatchPatternInfo> vals = cache.getList(cacheKeyOnList(matchId), MatchPatternInfo.class);
        if (vals.size() == 0) {
            vals = patternMapper.gets(matchId);
            cache.set(cacheKeyOnList(matchId), vals, TimeUnit.SECONDS.convert(5, TimeUnit.DAYS));
        }
        return vals;
    }

    @Override
    public MatchPatternInfo get(int id) {
        MatchPatternInfo val = cache.get(cacheKey(id), MatchPatternInfo.class);
        if (val == null) {
            val = patternMapper.get(id);
            cache.set(cacheKey(id), val, TimeUnit.SECONDS.convert(5, TimeUnit.DAYS));
        }
        return val;
    }
}
