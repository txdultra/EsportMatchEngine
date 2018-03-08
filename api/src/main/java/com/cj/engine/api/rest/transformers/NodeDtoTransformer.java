package com.cj.engine.api.rest.transformers;

import com.cj.engine.core.EnrollPlayer;
import com.cj.engine.core.VsNode;
import com.cj.engine.storage.IEnrollPlayerStorage;
import com.cj.engine.transfomers.DocTransformer;
import com.cj.engine.api.rest.result.EnrollerDto;
import com.cj.engine.api.rest.result.NodeDto;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Create Time: 2018年03月02日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class NodeDtoTransformer implements DocTransformer<VsNode,NodeDto> {

    @Autowired
    private IEnrollPlayerStorage playerStorage;

    @Autowired
    private DocTransformer<EnrollPlayer,EnrollerDto> playerTransformer;

    @Override
    public NodeDto transform(VsNode data) {
        NodeDto dto = new NodeDto();
        dto.setId(data.getId());
        dto.setWinnerNextId(data.getWinNextId());
        dto.setLoseNextId(data.getLoseNextId());
        dto.setGroupId(data.getGroupId());
        dto.setPatternId(data.getPatternId());
        dto.setMatchId(data.getMatchId());
        dto.setIndex(data.getIndex());
        dto.setPlayerId(data.getPlayerId());
        dto.setState(data.getState());
        dto.setScore(data.getScore());
        dto.setRound(data.getRound());
        dto.setEmpty(data.isEmpty());
        dto.setParentId(data.getSrcNodeId());
        dto.setProperties(data.getMap());

        if(!Strings.isNullOrEmpty(data.getPlayerId())) {
            EnrollPlayer player = playerStorage.get(data.getPlayerId(),data.getMatchId());
            dto.setPlayer(playerTransformer.transform(player));
        }
        return dto;
    }
}
