package com.cj.engine.core;

import com.cj.engine.core.cfg.MPCfg;
import com.cj.engine.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by tang on 2016/3/15.
 *
 * @author tangxd
 */
public abstract class AbstractMatchPattern {
//    private AbstractMatchPattern previousPattern = null;
//    private AbstractMatchPattern nextPattern = null;
    private PatternStates state = PatternStates.UnBuildSchedule;

    @Autowired
    private IMatchRoundService matchRoundService;

    @Autowired
    private IVsGroupService vsGroupService;

    @Autowired
    private IVsNodeService vsNodeService;

    @Autowired
    private IEnrollPlayerService enrollPlayerService;

    @Autowired
    private IPlayerAssignStrategy assignStrategy;

    @Autowired
    private IPatternService patternService;

    @Autowired
    private ICommonService commonService;

    protected MPCfg cfg;
    protected boolean initialized = false;
    protected int maxRound;
    protected int schedulePlayers;
    /**
     * 赛事模型数据
     */
    protected Map<String, MatchRound> rounds = new LinkedHashMap<>();
    protected Map<String, VsGroup> groups = new LinkedHashMap<>();
    protected Map<String, VsNode> nodes = new LinkedHashMap<>();
    protected Map<Integer, EnrollPlayer> players = new LinkedHashMap<>();

    public AbstractMatchPattern(MPCfg cfg) {
        this.cfg = cfg;
    }

    /**
     * 初始化模型数据
     */
    public synchronized final void init() {
        //如果已初始化则略过
        if (this.initialized && cfg.isInitialized()) {
            return;
        }
        //装载模型结构
        Collection<MatchRound> rounds = matchRoundService.getRounds(cfg.getPatternId());
        for (MatchRound round : rounds) {
            this.rounds.put(round.getId(), round);
        }
        Collection<VsGroup> groups = vsGroupService.getGroups(cfg.getPatternId());
        for (VsGroup group : groups) {
            this.groups.put(group.getId(), group);
        }
        Collection<VsNode> nodes = vsNodeService.getNodes(cfg.getPatternId());
        for (VsNode node : nodes) {
            this.nodes.put(node.getId(), node);
        }
        //装载选手
        Collection<EnrollPlayer> players = enrollPlayerService.getPlayers(cfg.getMatchId());
        for (EnrollPlayer p : players) {
            this.players.put(p.getPlayerId(), p);
        }

        this.maxRound = rounds.size();
        this.initialize();
        state = patternService.getState(this.getPatternId());
        //this.initCacheData();

        //加载扩展数据
        this.initExtend();

        //下一阶段初始化
//        if(this.getNextPattern() != null) {
//            this.getNextPattern().init();
//        }
    }

    private synchronized void initialize() {
        this.initialized = true;
        this.cfg.setInitialized(true);
    }

    protected abstract void initExtend();

//    private void initCacheData() {
//        if (cache == null) {
//            return;
//        }
//        cache.multiAddMatchRounds(this.getPatternId(), this.rounds.values());
//        for (MatchRound mr : this.rounds.values()) {
//            Collection<VsGroup> groups = getVsGroups(mr.getId());
//            cache.multiAddVsGroups(mr.getId(), groups);
//            for (VsGroup group : groups) {
//                Collection<VsNode> nodes = getVsNodes(group.getId());
//                cache.multiAddVsNodes(group.getId(), nodes);
//            }
//        }
//    }

    /**
     * 最大轮次
     */
    public int maxRound() {
        return this.maxRound;
    }

    public boolean initialized() {
        return this.initialized;
    }

    public PatternStates state() {
        return this.state;
    }

    public MatchRound getMatchRound(int round) {
        for (MatchRound mr : this.rounds.values()) {
            if (mr.getRound() == round) {
                return mr;
            }
        }
        return null;
    }

    /**
     * 赛事模型配置
     */
    public MResult setCfg(MPCfg cfg) {
        if (this.state.value() > PatternStates.UnBuildSchedule.value()) {
            return new MResult("1001", "禁止修改参数");
        }
        this.cfg = cfg;
        return new MResult(MResult.SUCCESS_CODE, "修改成功");
    }

    public MPCfg getCfg() {
        return this.cfg;
    }

    public String getPatternId() {
        return this.cfg.getPatternId();
    }

    //上一阶段赛制
//    public AbstractMatchPattern getPreviousPattern() {
//        return previousPattern;
//    }
//
//    public AbstractMatchPattern setPreviousPattern(AbstractMatchPattern previousPattern) {
//        this.previousPattern = previousPattern;
//        return previousPattern;
//    }

    //下一阶段赛制
//    public AbstractMatchPattern getNextPattern() {
//        return nextPattern;
//    }
//
//    public AbstractMatchPattern setNextPattern(AbstractMatchPattern nextPattern) {
//        this.nextPattern = nextPattern;
//        return nextPattern;
//    }
//
//    public void setAssignStrategy(IPlayerAssignStrategy strategy) {
//        this.assignStrategy = strategy;
//    }

    public synchronized MResult buildSchedule(int players) {
        if (this.state.value() >= PatternStates.BuildedSchedule.value()) {
            return new MResult("1102", "当前状态不允许构建赛程");
        }
        this.schedulePlayers = players;
        MResult mResult = this.verifyNewPattern();
        if (!mResult.getCode().equals(MResult.SUCCESS_CODE)) {
            this.reset();
            return mResult;
        }
        mResult = this.newSchedule(players);
        if (!mResult.getCode().equals(MResult.SUCCESS_CODE)) {
            this.reset();
            return mResult;
        }
        this.state = PatternStates.BuildedSchedule;
//        if (this.getNextPattern() != null) {
//            return this.getNextPattern().buildSchedule(this.promotionNextPatternPlayerCounts());
//        }
        return mResult;
    }

    /**
     * 赛程模型回档
     */
    protected synchronized void reset() {
        this.maxRound = 0;
        this.schedulePlayers = 0;
        this.rounds.clear();
        this.groups.clear();
        this.nodes.clear();
        this.players.clear();
        this.initialized = false;
        this.state = PatternStates.UnBuildSchedule;
        //回到数据库状态
        this.init();
        //初始化上一阶段
//        if(this.getPreviousPattern() != null) {
//            this.getPreviousPattern().reset();
//        }
    }

    /**
     * 是否允许开盘
     *
     * @return
     */
    public abstract MResult verifyNewPattern();

    /**
     * 构建赛程
     *
     * @param players 设置的报名人数(实际报名人数=设置人数-种子人数)
     * @return
     */
    protected abstract MResult newSchedule(int players);

    /**
     * 获取赛程
     *
     * @return
     */
    public List<MatchSchedule> getSchedules() {
        List<MatchSchedule> mss = new ArrayList<>();
        MatchSchedule ms = new MatchSchedule();
        ms.addAll(this.rounds.values());
        mss.add(ms);
        return mss;
    }

    public List<VsGroup> getVsGroups(String roundId) {
        List<VsGroup> groups = new ArrayList<>();
        this.groups.forEach((a, b) -> {
            if (Objects.equals(b.getRoundId(), roundId))
                groups.add(b);
        });
        return groups;
    }

    public List<VsGroup> getVsGroups(int round) {
        MatchRound mr = this.getMatchRound(round);
        if (mr == null)
            return new ArrayList<>();
        return getVsGroups(mr.getId());
    }

    public List<VsNode> getVsNodes(String groupId) {
        //cache调用
        List<VsNode> nodes = new ArrayList<>();
        this.nodes.forEach((a, b) -> {
            if (b.getGroupId().equals(groupId))
                nodes.add(b);
        });
        return nodes;
    }

    public EnrollPlayer getPlayer(int playerId) {
        AbstractMatchPattern mp = getFirstPattern();
        return mp.getEnrollPlayer(playerId);
    }

    private EnrollPlayer getEnrollPlayer(int playerId) {
        return this.players.get(playerId);
    }

//    protected AbstractMatchPattern getFirstPattern() {
//        AbstractMatchPattern mp = this;
//        for (; ; ) {
//            if (mp.previousPattern != null)
//                mp = mp.previousPattern;
//            else
//                break;
//        }
//        return mp;
//    }

    //装载报名选手
    public MResult loadPlayers(Collection<EnrollPlayer> players) {
//        if (this.previousPattern != null)
//            return new MResult("1003", "非第一阶段不能载入选手");
        for (EnrollPlayer p : players) {
            this.players.put(p.getPlayerId(), p);
        }
        this.schedulePlayers = this.players.size();
        return new MResult(MResult.SUCCESS_CODE, "成功");
    }

    //分配选手进入赛程
    public MResult assignPlayers() {
//        if (this.previousPattern != null)
//            return new MResult("1002", "非第一阶段无需分配选手");
        Collection<EnrollPlayer> players = this.players.values();
        MResult result = this.assignStrategy.assign(this, players);
        if (result.getCode().equals(MResult.SUCCESS_CODE)) {
            dataProvider.batchSaveEnrollPlayerStartNode(players);
            this.assignedPlayersEvent();
            this.state = PatternStates.AssignedPlayers;
        }
        return result;
    }

    /**
     * 分配选手后事件处理
     */
    protected abstract void assignedPlayersEvent();

    /**
     * 构建对阵数据
     */
    public boolean establishVs() {
        if (this.state != PatternStates.AssignedPlayers)
            return false;
        initEstablishVs();
        this.state = PatternStates.EstablishedVs;
        return true;
    }

    protected abstract void initEstablishVs();

    public void save() {
        //保持参数
        commonService.saveMPCfg(this.cfg);
        //未确定真实人数时不保存赛程模型
        if (this.players.size() == 0) {
            return;
        }
        //保持模型
        for (MatchRound mr : this.rounds.values()) {
            if (mr.isModified()) {
                matchRoundService.save(mr);
            }
        }

        for (VsGroup group : this.groups.values()) {
            if (group.isModified()) {
                vsGroupService.save(group);
            }
        }

        for (VsNode node : this.nodes.values()) {
            if (node.isModified()) {
                vsNodeService.save(node);
            }
        }
        //保存模型状态
        patternService.saveState(this.getPatternId(), state);
    }

    /**
     * 轮次的晋级选手
     * @param round
     * @return
     */
    public abstract Collection<EnrollPlayer> roundPromotionPlayers(int round);

    /**
     * 处理结果
     * @param vs
     * @return
     */
    public MResult handleVs(MatchVs vs) {
        switch (vs.getState()) {
            case Confirmed:
                return confirmedVs(vs);
            case UnConfirm:
                return unConfirmVs(vs);
            default:
                return new MResult("1101", "状态错误");
        }
    }

    protected abstract MResult unConfirmVs(MatchVs vs);

    protected abstract MResult confirmedVs(MatchVs vs);

    //计算排名
    //public abstract Collection<EnrollPlayer> ranking(int tops);

    /**
     * 下一个选手
     * @param playerId
     * @return
     */
    public abstract EnrollPlayer nextVsPlayer(int playerId);

    /**
     * 某论对阵列表
     * @param round
     * @param otherArgs
     * @return
     */
    public abstract Collection<MatchVs> getRoundVsList(int round, Map<String, Object> otherArgs);

    /**
     * 选手最后位置
     *
     * @param playerId
     * @return
     */
    protected VsNode playerLastNode(int playerId) {
        MatchRound firstMr = this.getMatchRound(1);
        Collection<VsGroup> groups = this.getVsGroups(firstMr.getId());
        VsNode playerFirstNode = null;
        for (VsGroup group : groups) {
            for (VsNode node : this.getVsNodes(group.getId())) {
                if (node.getPlayerId() == playerId) {
                    playerFirstNode = node;
                }
            }
            if (playerFirstNode != null)
                break;
        }
        return playerNextNode(playerFirstNode);
    }

    private VsNode playerNextNode(VsNode node) {
        if (node == null)
            return null;
        if (node.getPlayerId() == 0)
            return null;
        VsNode nextNode = this.nodes.get(node.getWinNextId());
        if (nextNode.getPlayerId() == 0)
            return node;
        return playerNextNode(nextNode);
    }

    /**
     * 位置互换
     * @param n1key
     * @param n2key
     * @return
     */
    public MResult exchangeNodePlayer(String n1key, String n2key) {
        VsNode vs1 = this.nodes.get(n1key);
        VsNode vs2 = this.nodes.get(n2key);
        if (vs1 == null || vs2 == null) {
            return new MResult("1001", "节点不存在");
        }
        int player1id = vs1.getPlayerId();
        int player2id = vs2.getPlayerId();
        vs1.setPlayerId(player2id);
        vs1.modify();
        vs2.setPlayerId(player1id);
        vs2.modify();
        return new MResult(MResult.SUCCESS_CODE, "交换成功");
    }

//    @Override
//    public int promotionNextPatternPlayerCounts() {
//        if(this.schedulePlayers <= this.cfg.getPromotionCounts())
//            return this.schedulePlayers;
//        return this.cfg.getPromotionCounts();
//    }

    ///////////////////////////////////////////////////////////////////////
    //help method
    ///////////////////////////////////////////////////////////////////////

    protected VsGroup newVsGroup(int round, int index, String roundId, int nodes, boolean cache) {
        VsGroup group = new VsGroup(MatchHelper.getItemId(this.cfg.getType(), PatternItemTypes.Group), nodes);
        group.setCurrentRound(round);
        group.setIndex(index);
        group.setRoundId(roundId);
        group.setPatternId(this.cfg.getPatternId());
        group.modify();
        for (Integer i = 0; i < nodes; i++) {
            VsNode node = new VsNode(MatchHelper.getItemId(this.cfg.getType(), PatternItemTypes.Node));
            node.setIndex(i);
            node.setPatternId(this.cfg.getPatternId());
            node.setGroupId(group.getId());
            node.modify();
            this.nodes.put(node.getId(), node);
        }
        if (cache)
            this.groups.put(group.getId(), group);
        return group;
    }

    protected boolean is2Pow(int c) {
        int i = 1;
        while (i != 0) {
            i = i << 1;
            if (i >= c)
                break;
        }
        return i == c;
    }

    /**
     * 上一阶段进入下一阶段
     * @param srcNode
     */
//    @Override
//    public void receivePrevPattern(VsNode srcNode) {
//        if(srcNode == null) return;
//        MatchRound mr = this.getMatchRound(1);
//        Collection<VsGroup> groups = this.getVsGroups(mr.getId());
//        for(VsGroup group:groups) {
//            VsNode emptyNode = null;
//            for(VsNode node:this.getVsNodes(group.getId())){
//                if(node.getPlayerId() == 0 && !node.isBye()){
//                    emptyNode = node;
//                    break;
//                }
//            }
//            if(emptyNode != null) {
//                emptyNode.setInsert(true);
//                emptyNode.setPlayerId(srcNode.getPlayerId());
//                emptyNode.setSrcNodeId(srcNode.getId());
//                emptyNode.modify();
//            }
//        }
//    }
}
