package com.cj.engine.api.rest.transformers;

import com.cj.engine.api.rest.result.*;
import com.cj.engine.core.*;
import com.cj.engine.transfomers.DocTransformer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Create Time: 2018年03月07日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class PreviewScheduleTransformer implements DocTransformer<IMatchEngine, MatchDto> {
    @Override
    public MatchDto transform(IMatchEngine engine) {
        MatchDto dto = new MatchDto();
        dto.setId(0);
        dto.setTitle("赛事预览");
        dto.setState(MatchStates.Initialized);
        dto.setPatternIds(engine.getPatterns().stream().map(AbstractMatchPattern::getPatternId).collect(Collectors.toCollection(ArrayList::new)));
        Collection<PatternDto> patternDtos = new ArrayList<>();
        for (AbstractMatchPattern p : engine.getPatterns()) {
            PatternDto pDto = new PatternDto();
            pDto.setId(p.getPatternId());
            pDto.setMatchId(p.getCfg().getMatchId());
            pDto.setType(p.getCfg().getType());
            pDto.setIndex(p.getCfg().getIndex());
            pDto.setPromotions(p.getCfg().getPromotions());
            pDto.setGroupPlayers(p.getCfg().getGroupPlayerNumber());
            pDto.setTitle(String.format("第%d阶段", p.getCfg().getIndex() + 1));

            pDto.setState(PatternStates.BuildedSchedule);
            Collection<MatchRound> rounds = p.getAllRounds().values().stream()
                    .sorted(Comparator.comparing(MatchRound::getRound))
                    .collect(Collectors.toCollection(ArrayDeque::new));
            for (MatchRound mr : rounds) {
                RoundDto rDto = new RoundDto();
                rDto.setId(mr.getId());
                rDto.setCategory(mr.getCategory());
                rDto.setPatternId(mr.getPatternId());
                rDto.setMatchId(mr.getMatchId());
                rDto.setRound(mr.getRound());
                rDto.setGroupCounts((short) mr.getGroupCounts());

                for (VsGroup group : p.getVsGroups(mr.getId())) {
                    GroupDto gDto = new GroupDto();
                    gDto.setId(group.getId());
                    gDto.setPlayers(group.getGroupPlayerCount());
                    gDto.setPatternId(group.getPatternId());
                    gDto.setMatchId(group.getMatchId());
                    gDto.setIndex(group.getIndex());
                    gDto.setWinners(group.getWinners());
                    gDto.setRound(group.getRound());
                    gDto.setRoundId(group.getRoundId());
                    gDto.setCategory(group.getCategory());
                    gDto.setState(group.getState());
                    gDto.setPostTime(group.getPostTime());

                    for (VsNode node : p.getVsNodes(group.getId())) {
                        NodeDto nDto = new NodeDto();
                        nDto.setId(node.getId());
                        nDto.setWinnerNextId(node.getWinNextId());
                        nDto.setLoseNextId(node.getLoseNextId());
                        nDto.setGroupId(node.getGroupId());
                        nDto.setPatternId(node.getPatternId());
                        nDto.setMatchId(node.getMatchId());
                        nDto.setIndex(node.getIndex());
                        nDto.setPlayerId(node.getPlayerId());
                        nDto.setState(node.getState());
                        nDto.setScore(node.getScore());
                        nDto.setRound(node.getRound());
                        nDto.setEmpty(node.isEmpty());
                        nDto.setParentId(node.getSrcNodeId());
                        gDto.getNodeVss().add(nDto);
                    }
                    rDto.getGroups().add(gDto);
                }
                pDto.getRounds().add(rDto);
            }
            patternDtos.add(pDto);
        }
        dto.setPatterns(patternDtos);
        return dto;
    }
}
