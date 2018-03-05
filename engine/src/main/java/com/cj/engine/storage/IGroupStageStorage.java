package com.cj.engine.storage;

import com.cj.engine.core.GroupStageRow;

import java.util.Collection;

public interface IGroupStageStorage {
    boolean save(GroupStageRow row);

    GroupStageRow get(String nodeId);

    Collection<GroupStageRow> gets(String groupId);

    void delByGroupId(String groupId);

    boolean saveOrUpdate(GroupStageRow row);
}
