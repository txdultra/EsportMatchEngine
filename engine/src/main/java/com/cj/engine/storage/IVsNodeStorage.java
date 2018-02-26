package com.cj.engine.storage;

import com.cj.engine.core.VsNode;

import java.util.Collection;

/**
 * 赛事节点接口
 */
public interface IVsNodeStorage {
    VsNode get(String id);

    Collection<VsNode> getNodes(int patternId);

    boolean create(VsNode node);

    boolean saveOrUpdate(VsNode node);

    void batchSave(Collection<VsNode> nodes);

    void delByPatternId(int patternId);
}
