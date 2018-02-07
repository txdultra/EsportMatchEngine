package com.cj.engine.service;

import com.cj.engine.core.VsGroup;

import java.util.Collection;

/**
 * 赛事小组接口
 */
public interface IVsGroupService {
    Collection<VsGroup> getGroups(String patternId);
    void save(VsGroup group);
}
