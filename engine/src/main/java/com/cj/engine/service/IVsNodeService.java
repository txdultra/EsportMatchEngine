package com.cj.engine.service;

import com.cj.engine.core.VsNode;

import java.util.Collection;

/**
 * 赛事节点接口
 */
public interface IVsNodeService {
    VsNode get(String id);

    Collection<VsNode> getNodes(int patternId);

    boolean save(VsNode node);

    void batchSave(Collection<VsNode> nodes);
}
