package com.cj.engine.storage.impl;

import com.cj.engine.cfg.caching.Cache;
import com.cj.engine.core.GroupStageRow;
import com.cj.engine.dao.MatchGroupStageMapper;
import com.cj.engine.storage.IGroupStageStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Service
public class GroupStageStorageImpl implements IGroupStageStorage {

    @Autowired
    private MatchGroupStageMapper stageMapper;

    @Autowired
    private Cache cache;

    private String cacheKey(String nodeId) {
        return String.format("cj-match:group_stage:entity:%s", nodeId);
    }

    @Override
    public boolean save(GroupStageRow row) {
        boolean ok = stageMapper.insert(row) > 0;
        if(ok) {
            cache.set(cacheKey(row.getNodeId()),row,TimeUnit.SECONDS.convert(5,TimeUnit.DAYS));
        }
        return ok;
    }

    @Override
    public boolean saveOrUpdate(GroupStageRow row) {
        boolean ok = stageMapper.upsert(row) > 0;
        if(ok) {
            cache.del(cacheKey(row.getNodeId()));
        }
        return ok;
    }

    @Override
    public GroupStageRow get(String nodeId) {
        GroupStageRow row = cache.get(cacheKey(nodeId), GroupStageRow.class);
        if(null == row) {
            row = stageMapper.get(nodeId);
            if(null != row) {
                cache.set(cacheKey(nodeId),row, TimeUnit.SECONDS.convert(5,TimeUnit.DAYS));
            }
        }
        return row;
    }

    @Override
    public void delByGroupId(String groupId) {
        Collection<String> nodeIds = stageMapper.getNodeIdsByGroupId(groupId);
        cache.dels(nodeIds.stream().map(this::cacheKey).toArray(String[]::new));
        stageMapper.delByGroupId(groupId);
    }
}
