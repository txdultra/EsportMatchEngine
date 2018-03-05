package com.cj.matchengine.api.rest.transformers;

import com.cj.engine.core.EnrollPlayer;
import com.cj.engine.transfomers.DocTransformer;
import com.cj.matchengine.api.rest.result.EnrollerDto;
import org.springframework.stereotype.Service;

/**
 * <p>Create Time: 2018年03月02日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class EnrollerDtoTransformer implements DocTransformer<EnrollPlayer,EnrollerDto> {
    @Override
    public EnrollerDto transform(EnrollPlayer data) {
        EnrollerDto dto = new EnrollerDto();
        dto.setPlayerId(data.getPlayerId());
        dto.setMatchId(data.getMatchId());
        dto.setNodeId(data.getNodeId());
        dto.setType(data.getType());
        dto.setLevelId(data.getLevelId());
        dto.setPostTime(data.getPostTime());
        dto.putAll(data.getMap());
        return dto;
    }
}
