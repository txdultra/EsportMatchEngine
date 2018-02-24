package com.cj.engine.service.impl;

import com.cj.engine.cfg.caching.Cache;
import com.cj.engine.core.MatchRound;
import com.cj.engine.dao.MatchRoundMapper;
import com.cj.engine.service.IMatchRoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MatchRoundServiceImpl implements IMatchRoundService {

    @Autowired
    private MatchRoundMapper roundMapper;

    @Autowired
    private Cache cache;

    private String cacheKey(String id) {
        return String.format("cj-match:round:%s", id);
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
    public boolean save(MatchRound round) {
        if(get(round.getId()) == null) {
            return roundMapper.insert(round) > 0;
        }
        long c = roundMapper.update(round);
        cache.del(cacheKey(round.getId()));
        return c > 0;
    }

    @Override
    public void batchSave(Collection<MatchRound> rounds) {
        List<MatchRound> mrs = new ArrayList<>();
        for (MatchRound mr : rounds) {
            if (mr.isModified()) {
                mrs.add(mr);
            }
        }
        roundMapper.batchInsert(mrs);
    }
}
