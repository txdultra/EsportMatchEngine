package com.cj.engine.storage.impl;

import com.cj.engine.cfg.caching.Cache;
import com.cj.engine.core.VsNode;
import com.cj.engine.dao.MatchNodeMapper;
import com.cj.engine.storage.IVsNodeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Service
public class VsNodeStorageImpl implements IVsNodeStorage {
    @Autowired
    private MatchNodeMapper nodeMapper;

    @Autowired
    private Cache cache;

    private String cacheKey(String id) {
        return String.format("cj-match:node:%s", id);
    }

    @Override
    public VsNode get(String id) {
        VsNode node = cache.get(cacheKey(id), VsNode.class);
        if (null == node) {
            node = nodeMapper.get(id);
            if (null != node) {
                cache.set(cacheKey(id), node, TimeUnit.SECONDS.convert(5, TimeUnit.DAYS));
            }
        }
        return node;
    }

    @Override
    public Collection<VsNode> getNodes(int patternId) {
        return nodeMapper.gets(patternId);
    }

    @Override
    public boolean create(VsNode node) {
        boolean ok = nodeMapper.insert(node) > 0;
        if (ok) {
            cache.set(cacheKey(node.getId()), node, TimeUnit.SECONDS.convert(5, TimeUnit.DAYS));
        }
        return ok;
    }

    @Override
    public boolean saveOrUpdate(VsNode node) {
        boolean ok = nodeMapper.upsert(node) > 0;
        if (ok) {
            cache.del(cacheKey(node.getId()));
        }
        return ok;
    }

    @Override
    public void batchSave(Collection<VsNode> nodes) {
        nodeMapper.batchInsert(nodes);
    }

    @Override
    public void delByPatternId(int patternId) {
        Collection<String> ids = nodeMapper.getIdsByPatternId(patternId);
        cache.dels(ids.stream().map(this::cacheKey).toArray(String[]::new));
        nodeMapper.delByPatternId(patternId);
    }
}
