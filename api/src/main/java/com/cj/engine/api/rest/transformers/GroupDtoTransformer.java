package com.cj.engine.api.rest.transformers;

import com.cj.engine.api.rest.result.GroupDto;
import com.cj.engine.api.rest.result.NodeDto;
import com.cj.engine.api.rest.result.VsDto;
import com.cj.engine.core.GroupStageRow;
import com.cj.engine.core.MatchVs;
import com.cj.engine.core.VsGroup;
import com.cj.engine.core.VsNode;
import com.cj.engine.storage.IGroupStageStorage;
import com.cj.engine.storage.IMatchVsStorage;
import com.cj.engine.storage.IVsNodeStorage;
import com.cj.engine.transfomers.DocTransformer;
import com.cj.engine.api.rest.result.GroupStageRowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * <p>Create Time: 2018年03月02日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class GroupDtoTransformer implements DocTransformer<VsGroup, GroupDto> {

    @Autowired
    private IVsNodeStorage nodeStorage;

    @Autowired
    private IMatchVsStorage vsStorage;

    @Autowired
    private IGroupStageStorage stageStorage;

    @Autowired
    private DocTransformer<VsNode, NodeDto> nodeTransformer;

    @Autowired
    private DocTransformer<MatchVs, VsDto> vsTransformer;

    @Autowired
    private DocTransformer<GroupStageRow, GroupStageRowDto> rowTransformer;

    @Override
    public GroupDto transform(VsGroup data) {
        GroupDto dto = new GroupDto();
        dto.setId(data.getId());
        dto.setPlayers(data.getGroupPlayerCount());
        dto.setPatternId(data.getPatternId());
        dto.setMatchId(data.getMatchId());
        dto.setIndex(data.getIndex());
        dto.setWinners(data.getWinners());
        dto.setRound(data.getRound());
        dto.setRoundId(data.getRoundId());
        dto.setCategory(data.getCategory());
        dto.setState(data.getState());
        dto.setPostTime(data.getPostTime());
        dto.setProperties(data.getMap());

        Collection<VsNode> nodes = nodeStorage.getNodes(data.getPatternId());
        Collection<NodeDto> nodeDtos = nodes.stream().map(nodeTransformer::transform).collect(Collectors.toCollection(ArrayList::new));
        dto.setNodeVss(nodeDtos);

        Collection<MatchVs> vss = vsStorage.getVss(data.getId());
        Collection<VsDto> vsDtos = vss.stream().map(vsTransformer::transform).collect(Collectors.toCollection(ArrayList::new));
        dto.setVss(vsDtos);

        Collection<GroupStageRow> rows = stageStorage.gets(data.getId());
        Collection<GroupStageRowDto> rowDtos = rows.stream().map(rowTransformer::transform).collect(Collectors.toCollection(ArrayList::new));
        dto.setStageRows(rowDtos);

        return dto;
    }
}
