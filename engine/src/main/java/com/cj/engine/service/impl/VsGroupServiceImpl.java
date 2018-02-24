package com.cj.engine.service.impl;

import com.cj.engine.cfg.caching.Cache;
import com.cj.engine.core.VsGroup;
import com.cj.engine.dao.MatchGroupMapper;
import com.cj.engine.service.IVsGroupService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class VsGroupServiceImpl implements IVsGroupService {

    @Autowired
    private MatchGroupMapper groupMapper;

    @Autowired
    private Cache cache;

    private String cacheKey(String id) {
        return String.format("cj-match:group:%s", id);
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
    public boolean save(VsGroup group) {
        if (get(group.getId()) == null) {
            return groupMapper.insert(group) > 0;
        }
        long c = groupMapper.update(group);
        cache.del(cacheKey(group.getId()));
        return c > 0;
    }


    @Override
    public void batchSave(Collection<VsGroup> groups) {

    }
}
