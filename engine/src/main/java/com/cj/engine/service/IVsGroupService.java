package com.cj.engine.service;

import com.cj.engine.core.VsGroup;

import java.util.Collection;
import java.util.List;

/**
 * 赛事小组接口
 */
public interface IVsGroupService {
    VsGroup get(String id);

    Collection<VsGroup> getGroups(int patternId, short category);

    boolean save(VsGroup group);

    void batchSave(Collection<VsGroup> groups);
}
