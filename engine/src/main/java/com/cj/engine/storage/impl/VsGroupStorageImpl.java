package com.cj.engine.storage.impl;

import com.cj.engine.cfg.caching.Cache;
import com.cj.engine.core.VsGroup;
import com.cj.engine.dao.MatchGroupMapper;
import com.cj.engine.storage.IVsGroupStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Service
public class VsGroupStorageImpl implements IVsGroupStorage {

    @Autowired
    private MatchGroupMapper groupMapper;

    @Autowired
    private Cache cache;

    private String cacheKey(String id) {
        return String.format("cj-match:group:entity:%s", id);
    }

    @Override
    public VsGroup get(String id) {
        VsGroup group = cache.get(cacheKey(id), VsGroup.class);
        if (null == group) {
            group = groupMapper.get(id);
            if (null != group) {
                cache.set(cacheKey(id), group, TimeUnit.SECONDS.convert(5, TimeUnit.DAYS));
            }
        }
        return group;
    }

    @Override
    public Collection<VsGroup> getGroups(int patternId, short category) {
        return groupMapper.gets(patternId, category);
    }

    @Override
    public boolean create(VsGroup group) {
        boolean ok = groupMapper.insert(group) > 0;
        if (ok) {
            cache.set(cacheKey(group.getId()), group, TimeUnit.SECONDS.convert(5, TimeUnit.DAYS));
        }
        return ok;
    }

    @Override
    public boolean saveOrUpdate(VsGroup group) {
        boolean ok = groupMapper.upsert(group) > 0;
        if (ok) {
            cache.del(cacheKey(group.getId()));
        }
        return ok;
    }

    @Override
    public void batchSave(Collection<VsGroup> groups) {
        groupMapper.batchInsert(groups);
    }

    @Override
    public void delByPatternId(int patternId) {
        Collection<String> ids = groupMapper.getIdsByPatternId(patternId);
        cache.dels(ids.stream().map(this::cacheKey).toArray(String[]::new));
        groupMapper.delByPatternId(patternId);
    }
}
