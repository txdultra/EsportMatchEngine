package com.cj.engine.service;

import com.cj.engine.core.VsNode;

import java.util.Collection;

/**
 * 赛事节点接口
 */
public interface IVsNodeService {
    Collection<VsNode> getNodes(String patternId);
    void save(VsNode node);

}
