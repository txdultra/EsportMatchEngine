package com.cj.engine.api.rest.transformers;

import com.cj.engine.core.MatchRound;
import com.cj.engine.model.MatchPatternInfo;
import com.cj.engine.storage.IMatchRoundStorage;
import com.cj.engine.transfomers.DocTransformer;
import com.cj.engine.api.rest.result.PatternDto;
import com.cj.engine.api.rest.result.RoundDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * <p>Create Time: 2018年03月02日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class PatternDtoTransformer implements DocTransformer<MatchPatternInfo,PatternDto> {

    @Autowired
    private IMatchRoundStorage roundStorage;

    @Autowired
    private DocTransformer<MatchRound,RoundDto> roundTransformer;

    @Override
    public PatternDto transform(MatchPatternInfo data) {
        PatternDto dto = new PatternDto();
        dto.setId(data.getId());
        dto.setMatchId(data.getMatchId());
        dto.setType(data.getType());
        dto.setBo(data.getBoN());
        dto.setState(data.getState());
        dto.setIndex(data.getIndex());
        dto.setTitle(data.getTitle());
        dto.setPromotions(data.getPromotions());
        dto.setGroupPlayers(data.getGroupPlayers());
        dto.setPostTime(data.getPostTime());
        dto.setProperties(data.getMap());

        Collection<MatchRound> rounds = roundStorage.getRounds(dto.getId());
        Collection<RoundDto> roundDtos = rounds.stream().map(roundTransformer::transform).collect(Collectors.toCollection(ArrayDeque::new));
        dto.setRounds(roundDtos);
        return dto;
    }
}
