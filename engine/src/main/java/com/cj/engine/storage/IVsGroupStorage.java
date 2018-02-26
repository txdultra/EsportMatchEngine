package com.cj.engine.storage;

import com.cj.engine.core.VsGroup;

import java.util.Collection;

/**
 * 赛事小组接口
 */
public interface IVsGroupStorage {
    VsGroup get(String id);

    Collection<VsGroup> getGroups(int patternId, short category);

    boolean create(VsGroup group);

    boolean saveOrUpdate(VsGroup group);

    void batchSave(Collection<VsGroup> groups);

    void delByPatternId(int patternId);
}
