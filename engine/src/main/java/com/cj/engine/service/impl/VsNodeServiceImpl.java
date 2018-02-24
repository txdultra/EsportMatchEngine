package com.cj.engine.service.impl;

import com.cj.engine.cfg.caching.Cache;
import com.cj.engine.core.VsNode;
import com.cj.engine.dao.MatchNodeMapper;
import com.cj.engine.service.IVsNodeService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Service
public class VsNodeServiceImpl implements IVsNodeService {
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
    public boolean save(VsNode node) {
        if (get(node.getId()) == null) {
            return nodeMapper.insert(node) > 0;
        }
        long c = nodeMapper.update(node);
        cache.del(cacheKey(node.getId()));
        return c > 0;
    }

    @Override
    public void batchSave(Collection<VsNode> nodes) {

    }
}
