package com.cj.engine.api.rest.transformers;

import com.cj.engine.core.EnrollPlayer;
import com.cj.engine.core.MatchVs;
import com.cj.engine.storage.IEnrollPlayerStorage;
import com.cj.engine.transfomers.DocTransformer;
import com.cj.engine.api.rest.result.EnrollerDto;
import com.cj.engine.api.rest.result.VsDto;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Create Time: 2018年03月02日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class VsDtoTransformer implements DocTransformer<MatchVs, VsDto> {

    @Autowired
    private IEnrollPlayerStorage playerStorage;

    @Autowired
    private DocTransformer<EnrollPlayer,EnrollerDto> enrollerTransformer;

    @Override
    public VsDto transform(MatchVs data) {
        VsDto dto = new VsDto();
        dto.setId(data.getId());
        dto.setLeftPlayerId(data.getLeftId());
        dto.setRightPlayerId(data.getRightId());
        dto.setLeftNodeId(data.getLeftNodeId());
        dto.setRightNodeId(data.getRightNodeId());
        dto.setLeftScore(data.getLeftScore());
        dto.setRightScore(data.getRightScore());
        dto.setWinnerPlayerId(data.getWinnerId());
        dto.setGroupId(data.getGroupId());
        dto.setMatchId(data.getMatchId());
        dto.setState(data.getState());
        dto.setProperties(data.getMap());

        if(!Strings.isNullOrEmpty(data.getLeftId())) {
            EnrollPlayer player = playerStorage.get(data.getLeftId(), data.getMatchId());
            if(null != player) {
                dto.setLeftPlayer(enrollerTransformer.transform(player));
            }
        }

        if(!Strings.isNullOrEmpty(data.getRightId())) {
            EnrollPlayer player = playerStorage.get(data.getRightId(), data.getMatchId());
            if(null != player) {
                dto.setRightPlayer(enrollerTransformer.transform(player));
            }
        }

        if(!Strings.isNullOrEmpty(data.getWinnerId())) {
            EnrollPlayer player = playerStorage.get(data.getWinnerId(), data.getMatchId());
            if(null != player) {
                dto.setWinnerPlayer(enrollerTransformer.transform(player));
            }
        }

        return dto;
    }
}
