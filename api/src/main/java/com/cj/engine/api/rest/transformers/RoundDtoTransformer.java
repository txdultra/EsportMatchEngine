package com.cj.engine.api.rest.transformers;

import com.cj.engine.api.rest.result.RoundDto;
import com.cj.engine.core.MatchRound;
import com.cj.engine.core.VsGroup;
import com.cj.engine.storage.IVsGroupStorage;
import com.cj.engine.transfomers.DocTransformer;
import com.cj.engine.api.rest.result.GroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * <p>Create Time: 2018年03月05日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class RoundDtoTransformer implements DocTransformer<MatchRound,RoundDto> {

    @Autowired
    private IVsGroupStorage groupStorage;

    @Autowired
    private DocTransformer<VsGroup,GroupDto> groupTransformer;

    @Override
    public RoundDto transform(MatchRound data) {
        RoundDto dto =  new RoundDto();
        dto.setId(data.getId());
        dto.setCategory(data.getCategory());
        dto.setPatternId(data.getPatternId());
        dto.setMatchId(data.getMatchId());
        dto.setRound(data.getRound());
        dto.setGroupCounts((short)data.getGroupCounts());
        dto.setProperties(data.getMap());

        Collection<VsGroup> groups = groupStorage.getGroups(data.getPatternId(),data.getCategory());
        Collection<GroupDto> dtos = groups.stream().map(groupTransformer::transform).collect(Collectors.toCollection(ArrayDeque::new));
        dto.setGroups(dtos);
        return dto;
    }
}
