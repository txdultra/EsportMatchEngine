package com.cj.engine.api.rest;

import com.alibaba.fastjson.JSON;
import com.cj.engine.api.rest.request.EnrollerRequest;
import com.cj.engine.api.rest.request.MatchRequest;
import com.cj.engine.api.rest.request.PatternRequest;
import com.cj.engine.api.rest.request.PreviewScheduleRequest;
import com.cj.engine.api.rest.result.*;
import com.cj.engine.cfg.SpringAppContext;
import com.cj.engine.core.*;
import com.cj.engine.core.cfg.BasePatternConfig;
import com.cj.engine.model.MatchInfo;
import com.cj.engine.model.MatchPatternInfo;
import com.cj.engine.storage.*;
import com.cj.engine.transfomers.DocTransformer;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>Create Time: 2018年03月01日</p>
 * <p>@author tangxd</p>
 **/
@RestController
@RequestMapping("v1")
@Api("赛事引擎API")
@Slf4j
public class RestApiController {

    @Autowired
    private IMatchStorage matchStorage;

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
    private DocTransformer<MatchInfo, MatchDto> matchTransformer;

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

    @Autowired
    private DocTransformer<IMatchEngine, MatchDto> previewTransformer;

    @Autowired
    private PatternConfigLoader cfgLoader;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //获取
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "获取赛事模型数据", response = Result.class)
    @RequestMapping(value = "match/pattern/{id}", method = RequestMethod.GET)
    public TResult<PatternDto> getMatchPatternData(
            @ApiParam(value = "模型id", required = true) @PathVariable("id") int id
    ) {
        if (id <= 0) {
            return new TResult<>("MP:0001", "id非法", null);
        }
        MatchPatternInfo pInfo = patternStorage.get(id);
        if (null == pInfo) {
            return new TResult<>("MP:0002", "模型不存在", null);
        }
        PatternDto dto = patternTransformer.transform(pInfo);
        return new TResult<>(Result.SUCCESS, "成功", dto);
    }

    @ApiOperation(value = "获取模型轮次数据", response = Result.class)
    @RequestMapping(value = "match/round/{id}", method = RequestMethod.GET)
    public TResult<RoundDto> getMatchRoundData(
            @ApiParam(value = "轮次id", required = true) @PathVariable("id") String id
    ) {
        if (Strings.isNullOrEmpty(id)) {
            return new TResult<>("MP:1001", "id非法", null);
        }
        MatchRound round = roundStorage.get(id);
        if (null == round) {
            return new TResult<>("MP:1002", "轮次不存在", null);
        }
        RoundDto dto = roundTransformer.transform(round);
        return new TResult<>(Result.SUCCESS, "成功", dto);
    }

    @ApiOperation(value = "获取模型组数据", response = Result.class)
    @RequestMapping(value = "match/group/{id}", method = RequestMethod.GET)
    public TResult<GroupDto> getMatchGroupData(
            @ApiParam(value = "组id", required = true) @PathVariable("id") String id
    ) {
        if (Strings.isNullOrEmpty(id)) {
            return new TResult<>("MP:1201", "id非法", null);
        }
        VsGroup group = groupStorage.get(id);
        if (null == group) {
            return new TResult<>("MP:1202", "小组不存在", null);
        }
        GroupDto dto = groupTransformer.transform(group);
        return new TResult<>(Result.SUCCESS, "成功", dto);
    }

    @ApiOperation(value = "获取模型组节点数据", response = Result.class)
    @RequestMapping(value = "match/node/{id}", method = RequestMethod.GET)
    public TResult<NodeDto> getMatchNodeData(
            @ApiParam(value = "节点id", required = true) @PathVariable("id") String id
    ) {
        if (Strings.isNullOrEmpty(id)) {
            return new TResult<>("MP:1301", "id非法", null);
        }
        VsNode node = nodeStorage.get(id);
        if (null == node) {
            return new TResult<>("MP:1302", "成员不存在", null);
        }
        NodeDto dto = nodeTransformer.transform(node);
        return new TResult<>(Result.SUCCESS, "成功", dto);
    }

    @ApiOperation(value = "获取对阵数据", response = Result.class)
    @RequestMapping(value = "match/vs/{id}", method = RequestMethod.GET)
    public TResult<VsDto> getMatchVsData(
            @ApiParam(value = "对阵id", required = true) @PathVariable("id") int id
    ) {
        if (id < 0) {
            return new TResult<>("MP:1401", "id非法", null);
        }
        MatchVs vs = vsStorage.get(id);
        if (null == vs) {
            return new TResult<>("MP:1402", "对阵不存在", null);
        }
        VsDto dto = vsTransformer.transform(vs);
        return new TResult<>(Result.SUCCESS, "成功", dto);
    }

    @ApiOperation(value = "获取赛事参赛选手数据", response = Result.class)
    @RequestMapping(value = "match/enrollers/{match_id}", method = RequestMethod.GET)
    public TResult<Collection<EnrollerDto>> getEnrollers(
            @ApiParam(value = "赛事id", required = true) @PathVariable("match_id") int matchId
    ) {
        if (matchId < 0) {
            return new TResult<>("MP:1501", "id非法", null);
        }
        Collection<EnrollPlayer> players = playerStorage.getPlayers(matchId);
        Collection<EnrollerDto> dtos = new ArrayList<>();
        for (EnrollPlayer player : players) {
            dtos.add(enrollerTransformer.transform(player));
        }
        return new TResult<>(Result.SUCCESS, "成功", dtos);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //生成
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "添加参赛选手", response = Result.class)
    @RequestMapping(value = "enroller", method = RequestMethod.POST)
    public Result addEnroller(
            @ApiParam(value = "选手数据", required = true) @RequestBody EnrollerRequest enroller, HttpServletRequest req
    ) {
        if (Strings.isNullOrEmpty(enroller.getPlayerId())) {
            return new Result("MP:1901", "player_id未设置");
        }
        if (enroller.getMatchId() < 0) {
            return new Result("MP:1902", "match_id未设置");
        }
        if (enroller.getType() == null) {
            return new Result("MP:1903", "type未设置");
        }
        MatchInfo match = matchStorage.get(enroller.getMatchId());
        if (match == null) {
            return new Result("MP:1905", "赛事不存在");
        }
        if (match.getState() != MatchStates.UnInitialize
                || match.getState() != MatchStates.Initialized) {
            return new Result("MP:1906", "赛事状态不能添加报名ß");
        }

        EnrollPlayer player = new EnrollPlayer();
        player.setPlayerId(enroller.getPlayerId());
        player.setMatchId(enroller.getMatchId());
        player.setType(enroller.getType());
        player.setLevelId(enroller.getLevelId());
        if (enroller.getProperties() != null) {
            player.setProperties(JSON.toJSONString(enroller.getProperties()));
        }
        boolean ok = playerStorage.save(player);
        if (!ok) {
            return new Result("MP:1904", "保存未成功");
        }
        return new Result(Result.SUCCESS, "保存成功");
    }

    @ApiOperation(value = "创建赛事", response = Result.class)
    @RequestMapping(value = "match", method = RequestMethod.POST)
    public Result createMatch(
            @ApiParam(value = "创建赛事数据", required = true) @RequestBody MatchRequest match, HttpServletRequest req
    ) {
        if (Strings.isNullOrEmpty(match.getTitle())) {
            return new Result("MP:1601", "title未设置");
        }
        MatchInfo m = new MatchInfo();
        m.setTitle(match.getTitle());
        if (match.getProperties() != null) {
            m.setProperties(JSON.toJSONString(match.getProperties()));
        }
        boolean ok = matchStorage.save(m);
        if (!ok) {
            return new Result("MP:1602", "保存未成功");
        }
        return new Result(Result.SUCCESS, "保存成功");
    }

    @ApiOperation(value = "创建赛事赛制", response = Result.class)
    @RequestMapping(value = "pattern", method = RequestMethod.POST)
    public Result createPattern(
            @ApiParam(value = "赛制数据", required = true) @RequestBody PatternRequest pattern, HttpServletRequest req
    ) {
        if (pattern.getMatchId() <= 0) {
            return new Result("MP:1702", "matchId未设置");
        }
        if (pattern.getType() == null) {
            return new Result("MP:1703", "type未设置");
        }
        if (pattern.getBo() <= 0) {
            return new Result("MP:1704", "bo未设置");
        }
        if (pattern.getPromotions() <= 0) {
            return new Result("MP:1705", "promotions必须大于0小于参赛人数");
        }
        if (pattern.getType() == PatternTypes.Group && pattern.getGroupPlayers() <= 0) {
            return new Result("MP:1706", "group_players必须大于0");
        }
        if (pattern.getIndex() < 0) {
            return new Result("MP:1707", "index必须大于等于0");
        }

        Collection<MatchPatternInfo> patternInfos = patternStorage.gets(pattern.getMatchId());
        for (MatchPatternInfo info : patternInfos) {
            if (info.getIndex() == pattern.getIndex()) {
                return new Result("MP:1708", String.format("已存在index=%d的赛制", pattern.getIndex()));
            }
        }

        MatchPatternInfo pi = new MatchPatternInfo();
        pi.setMatchId(pattern.getMatchId());
        pi.setType(pattern.getType());
        pi.setBoN(pattern.getBo());
        pi.setIndex(pattern.getIndex());
        pi.setPromotions(pattern.getPromotions());
        pi.setGroupPlayers(pattern.getGroupPlayers());
        if (pattern.getProperties() != null) {
            pi.setProperties(JSON.toJSONString(pattern.getProperties()));
        }
        if (Strings.isNullOrEmpty(pattern.getTitle())) {
            pi.setTitle(String.format("第%d阶段", pi.getIndex() + 1));
        } else {
            pi.setTitle(pattern.getTitle());
        }
        Optional<MatchPatternInfo> maxIndexPattern = patternInfos.stream().max(Comparator.comparing(MatchPatternInfo::getIndex));
        maxIndexPattern.ifPresent(matchPatternInfo -> pi.setPid(matchPatternInfo.getId()));

        boolean ok = patternStorage.save(pi);
        if (!ok) {
            return new Result("MP:1708", "保存未成功");
        }
        return new Result(Result.SUCCESS, "保存成功");
    }

    @ApiOperation(value = "构建赛事规划数据结构", response = Result.class)
    @RequestMapping(value = "match/build_schedule/{matchId}", method = RequestMethod.POST)
    public Result buildSchedule(
            @ApiParam(value = "赛事id", required = true) @PathVariable("matchId") int matchId,
            HttpServletRequest req
    ) {
        if (matchId <= 0) {
            return new Result("MP:1801", "matchId非法");
        }

        MatchInfo match = matchStorage.get(matchId);
        if (match == null) {
            return new Result("MP:1803", "赛事不存在");
        }

        Collection<MatchPatternInfo> patterns = patternStorage.gets(matchId);
        if (patterns.size() == 0) {
            return new Result("MP:1804", "未设置赛事中的赛制模型");
        }

        int players = playerStorage.counts(matchId);
        if (players <= 0) {
            return new Result("MP:1806", "报名人数小于0");
        }

        IMatchEngine engine = SpringAppContext.getBean(IMatchEngine.class, matchId);
        engine.init();
        MResult mr = engine.buildSchedule(players);
        if (mr.getCode().equals(MResult.SUCCESS_CODE)) {
            engine.save();
            return new Result(Result.SUCCESS, "生成成功");
        }
        return new Result("MP:1805", "生成失败");
    }

    @ApiOperation(value = "构建赛事规划预览数据结构", response = Result.class)
    @RequestMapping(value = "match/build_schedule/preview", method = RequestMethod.POST)
    public TResult<MatchDto> buildSchedulePreview(
            @ApiParam(value = "构建预览数据所需参数", required = true) @RequestBody PreviewScheduleRequest psr,
            HttpServletRequest req
    ) {
        if (psr.getMaxPlayers() <= 0) {
            return new TResult<>("MP:1901", "max_players未设置", null);
        }
        if (psr.getPatterns() == null || psr.getPatterns().size() == 0) {
            return new TResult<>("MP:1902", "未设置赛制", null);
        }
        short idx = 0;
        for (PatternRequest pr : psr.getPatterns()) {
            if (pr.getType() == null) {
                return new TResult<>("MP:1903", "type未设置", null);
            }
            if (pr.getIndex() != idx) {
                return new TResult<>("MP:1904", "index顺序错误", null);
            }
            if (pr.getPromotions() <= 0) {
                return new TResult<>("MP:1904", "promotions设置错误", null);
            }
            if (pr.getType() == PatternTypes.Group && pr.getGroupPlayers() <= 0) {
                return new TResult<>("MP:1905", "group_players设置错误", null);
            }
            idx++;
        }
        IMatchEngine engine = SpringAppContext.getBean(IMatchEngine.class, 0);
        engine.setIsPreview(true);
        engine.init();
        for (PatternRequest pr : psr.getPatterns()) {
            MatchPatternInfo info = new MatchPatternInfo();
            info.setGroupPlayers(pr.getGroupPlayers());
            info.setIndex(pr.getIndex());
            info.setPromotions(pr.getPromotions());
            info.setType(pr.getType());
            if (pr.getProperties() != null) {
                info.setProperties(JSON.toJSONString(pr.getProperties()));
            }
            BasePatternConfig cfg = cfgLoader.convert(info);
            engine.addPattern(cfg);
        }
        MResult mr = engine.buildSchedule(psr.getMaxPlayers());
        if (mr.getCode().equals(MResult.SUCCESS_CODE)) {
            MatchDto dto = previewTransformer.transform(engine);
            for (PatternDto pDto : dto.getPatterns()) {
                Optional<PatternRequest> pr = psr.getPatterns().stream().filter(a -> a.getIndex() == pDto.getIndex()).findFirst();
                pr.ifPresent(patternRequest -> pDto.setProperties(patternRequest.getProperties()));
            }

            //转化输出
            return new TResult<>(Result.SUCCESS, "OK", dto);
        }
        return new TResult<>("MP:1906", "获取失败", null);
    }

    @ApiOperation(value = "获取赛事赛制数据结构", response = Result.class)
    @RequestMapping(value = "match/schedule/{matchId}", method = RequestMethod.GET)
    public TResult<MatchDto> getSchedule(
            @ApiParam(value = "赛事id", required = true) @PathVariable("matchId") int matchId,
            HttpServletRequest req
    ) {
        if (matchId <= 0) {
            return new TResult<>("MP:2001", "matchId非法", null);
        }
        MatchInfo match = matchStorage.get(matchId);
        if (match == null) {
            return new TResult<>("MP:2002", "赛事不存在", null);
        }
        if (match.getState() != MatchStates.UnInitialize) {
            return new TResult<>("MP:2003", "赛事模型还未生成", null);
        }
        MatchDto dto = matchTransformer.transform(match);
        dto.setPatterns(new ArrayList<>());
        for (Integer patternId : dto.getPatternIds()) {
            MatchPatternInfo mpi = patternStorage.get(patternId);
            if (mpi != null) {
                PatternDto pDto = patternTransformer.transform(mpi);
                dto.getPatterns().add(pDto);
            }
        }
        return new TResult<>(Result.SUCCESS, "成功获取赛事模型", dto);
    }

    @ApiOperation(value = "分配选手到赛制上", response = Result.class)
    @RequestMapping(value = "match/assign_player/{matchId}", method = RequestMethod.POST)
    public Result assignPlayer(
            @ApiParam(value = "赛事id", required = true) @PathVariable("matchId") int matchId,
            HttpServletRequest req
    ) {
        if (matchId <= 0) {
            return new Result("MP:2101", "matchId非法");
        }
        MatchInfo match = matchStorage.get(matchId);
        if (match == null) {
            return new Result("MP:2102", "赛事不存在");
        }
        if (match.getState() != MatchStates.Initialized) {
            return new Result("MP:2103", "当前赛事模型不能分配选手");
        }
        IMatchEngine engine = SpringAppContext.getBean(IMatchEngine.class, matchId);
        engine.init();
        MResult result = engine.assignPlayers();
        if (result.getCode().equals(MResult.SUCCESS_CODE)) {
            return new Result(Result.SUCCESS, "成功分配选手");
        }
        return new Result("MP:2104", result.getMessage());
    }
}
