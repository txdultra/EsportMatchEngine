package com.cj.engine.storage;

import com.cj.engine.core.GroupStageRow;

public interface IGroupStageStorage {
    boolean save(GroupStageRow row);

    GroupStageRow get(String nodeId);

    void delByGroupId(String groupId);

    boolean saveOrUpdate(GroupStageRow row);
}
