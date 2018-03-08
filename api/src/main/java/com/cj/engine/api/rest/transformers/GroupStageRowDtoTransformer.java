package com.cj.engine.api.rest.transformers;

import com.cj.engine.api.rest.result.GroupStageRowDto;
import com.cj.engine.core.GroupStageRow;
import com.cj.engine.transfomers.DocTransformer;
import org.springframework.stereotype.Service;

/**
 * <p>Create Time: 2018年03月02日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class GroupStageRowDtoTransformer implements DocTransformer<GroupStageRow,GroupStageRowDto> {

    @Override
    public GroupStageRowDto transform(GroupStageRow data) {
        GroupStageRowDto dto = new GroupStageRowDto();
        dto.setNodeId(data.getNodeId());
        dto.setGroupId(data.getGroupId());
        dto.setWins(data.getWins());
        dto.setLoses(data.getLoses());
        dto.setPings(data.getPings());
        dto.setScores(data.getScores());
        return dto;
    }
}
