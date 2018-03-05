package com.cj.matchengine.api.rest;

import com.cj.engine.core.*;
import com.cj.engine.model.MatchPatternInfo;
import com.cj.engine.storage.*;
import com.cj.engine.transfomers.DocTransformer;
import com.cj.matchengine.api.rest.result.*;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Create Time: 2018年03月01日</p>
 * <p>@author tangxd</p>
 **/
@RestController
@RequestMapping("v1")
public class RestApiController {

    @Autowired
    private IPatternStorage patternStorage;

    @Autowired
    private IMatchRoundStorage roundStorage;

    @Autowired
    private IVsGroupStorage groupStorage;

    @Autowired
    private IVsNodeStorage nodeStorage;

    @Autowired
    private IMatchVsStorage vsStorage;

    @Autowired
    private IEnrollPlayerStorage playerStorage;

    @Autowired
    private DocTransformer<MatchPatternInfo, PatternDto> patternTransformer;

    @Autowired
    private DocTransformer<MatchRound, RoundDto> roundTransformer;

    @Autowired
    private DocTransformer<VsGroup, GroupDto> groupTransformer;

    @Autowired
    private DocTransformer<VsNode, NodeDto> nodeTransformer;

    @Autowired
    private DocTransformer<MatchVs, VsDto> vsTransformer;

    @Autowired
    private DocTransformer<EnrollPlayer, EnrollerDto> enrollerTransformer;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //获取
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "match/pattern/{id}",method = RequestMethod.GET)
    public TResult<PatternDto> getMatchPatternData(@PathVariable("id") int id) {
        if (id <= 0) {
            return new TResult<>("MP:0001", "id非法", null);
        }
        MatchPatternInfo pInfo = patternStorage.get(id);
        if (null == pInfo) {
            return new TResult<>("MP:0002", "模型不存在", null);
        }
        PatternDto dto = patternTransformer.transform(pInfo);
        return new TResult<>(TResult.SUCCESS, "成功", dto);
    }

    @RequestMapping(value = "match/round/{id}",method = RequestMethod.GET)
    public TResult<RoundDto> getMatchRoundData(@PathVariable("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            return new TResult<>("MP:1001", "id非法", null);
        }
        MatchRound round = roundStorage.get(id);
        if (null == round) {
            return new TResult<>("MP:1002", "轮次不存在", null);
        }
        RoundDto dto = roundTransformer.transform(round);
        return new TResult<>(TResult.SUCCESS, "成功", dto);
    }

    @RequestMapping(value = "match/group/{id}",method = RequestMethod.GET)
    public TResult<GroupDto> getMatchGroupData(@PathVariable("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            return new TResult<>("MP:2001", "id非法", null);
        }
        VsGroup group = groupStorage.get(id);
        if (null == group) {
            return new TResult<>("MP:2002", "小组不存在", null);
        }
        GroupDto dto = groupTransformer.transform(group);
        return new TResult<>(TResult.SUCCESS, "成功", dto);
    }

    @RequestMapping(value = "match/node/{id}",method = RequestMethod.GET)
    public TResult<NodeDto> getMatchNodeData(@PathVariable("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            return new TResult<>("MP:3001", "id非法", null);
        }
        VsNode node = nodeStorage.get(id);
        if (null == node) {
            return new TResult<>("MP:3002", "成员不存在", null);
        }
        NodeDto dto = nodeTransformer.transform(node);
        return new TResult<>(TResult.SUCCESS, "成功", dto);
    }

    @RequestMapping(value = "match/vs/{id}",method = RequestMethod.GET)
    public TResult<VsDto> getMatchVsData(@PathVariable("id") int id) {
        if (id < 0) {
            return new TResult<>("MP:4001", "id非法", null);
        }
        MatchVs vs = vsStorage.get(id);
        if (null == vs) {
            return new TResult<>("MP:4002", "对阵不存在", null);
        }
        VsDto dto = vsTransformer.transform(vs);
        return new TResult<>(TResult.SUCCESS, "成功", dto);
    }

    @RequestMapping(value = "match/enrollers/{match_id}",method = RequestMethod.GET)
    public TResult<Collection<EnrollerDto>> getEnrollers(@PathVariable("match_id") int matchId) {
        if (matchId < 0) {
            return new TResult<>("MP:4001", "id非法", null);
        }
        Collection<EnrollPlayer> players = playerStorage.getPlayers(matchId);
        Collection<EnrollerDto> dtos = new ArrayList<>();
        for (EnrollPlayer player : players) {
            dtos.add(enrollerTransformer.transform(player));
        }
        return new TResult<>(TResult.SUCCESS, "成功", dtos);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //生成
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "match/create", method = RequestMethod.POST)
    public TResult createMatch(HttpServletRequest request) {

    }
}
