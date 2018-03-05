package com.cj.engine.core;

import com.cj.engine.core.cfg.BasePatternConfig;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * Created by tang on 2016/3/15.
 *
 * @author tangxd
 */
@Slf4j
public abstract class AbstractMatchPattern {

    private PatternStates state = PatternStates.UnBuildSchedule;

    protected IDataService dataService;

//    private final static ExecutorService THREAD_POOL = ThreadPoolFactory.newThreadPool("match-save-%d",10,30,3000);

    /**
     * 赛事配置
     */
    private BasePatternConfig cfg;

    /**
     * 赛程次序
     */
    protected int index;

    /**
     * 是否已初始化
     */
    protected boolean initialized;
    /**
     * 最大轮次
     */
    protected int maxRound;
    /**
     * 计划参赛人数
     */
    protected int schedulePlayers;

    /**
     * 是否是第一次加载
     */
    private boolean first;

    /**
     * 赛事模型数据
     */
    protected Map<String, MatchRound> rounds = new LinkedHashMap<>();
    protected Map<String, VsGroup> groups = new LinkedHashMap<>();
    protected Map<String, VsNode> nodes = new LinkedHashMap<>();
    protected Map<String, EnrollPlayer> players = new LinkedHashMap<>();

    public AbstractMatchPattern(BasePatternConfig cfg, IDataService dataService) {
        this.cfg = cfg;
        this.dataService = dataService;
    }

    /**
     * 初始化模型数据
     */
    public synchronized final void init() {
        //如果已初始化则略过
        if (this.initialized) {
            return;
        }
        //装载模型结构
        Collection<MatchRound> rounds = dataService.getMatchRoundStorage().getRounds(cfg.getPatternId(), (short) 0);
        for (MatchRound round : rounds) {
            this.rounds.put(round.getId(), round);
        }
        Collection<VsGroup> groups = dataService.getVsGroupStorage().getGroups(cfg.getPatternId(), (short) 0);
        for (VsGroup group : groups) {
            this.groups.put(group.getId(), group);
        }
        Collection<VsNode> nodes = dataService.getVsNodeStorage().getNodes(cfg.getPatternId());
        for (VsNode node : nodes) {
            this.nodes.put(node.getId(), node);
        }
        //装载选手
        Collection<EnrollPlayer> players = dataService.getEnrollPlayerStorage().getPlayers(cfg.getMatchId());
        for (EnrollPlayer p : players) {
            this.players.put(p.getPlayerId(), p);
        }

        this.maxRound = rounds.size();

        state = dataService.getPatternStorage().getState(this.getPatternId());
        this.initialize();
        //加载扩展数据
        this.initExt();
    }

    private synchronized void initialize() {
        this.initialized = true;
        //是否第一次初始化
        if (this.state == PatternStates.UnBuildSchedule) {
            this.first = true;
        }
    }

    protected abstract void initExt();

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
    public MResult setCfg(BasePatternConfig cfg) {
        if (this.state != PatternStates.UnBuildSchedule) {
            return new MResult("1001", "禁止修改参数");
        }
        this.cfg = cfg;
        return new MResult(MResult.SUCCESS_CODE, "修改成功");
    }

    public BasePatternConfig getCfg() {
        return this.cfg;
    }

    public int getPatternId() {
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
//        this.getAssignStrategy = strategy;
//    }

    @Transactional(rollbackFor = Exception.class)
    public synchronized MResult buildSchedule(int players) {
        if (this.state.code() >= PatternStates.BuildedSchedule.code()) {
            return new MResult("1102", "当前状态不允许构建赛程");
        }
        this.schedulePlayers = players;
        MResult mResult = this.verifyNewPattern();
        if (!mResult.getCode().equals(MResult.SUCCESS_CODE)) {
            this.reset();
            return mResult;
        }
        mResult = this.initSchedule(players);
        if (!mResult.getCode().equals(MResult.SUCCESS_CODE)) {
            this.reset();
            return mResult;
        }
        this.state = PatternStates.BuildedSchedule;
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

        this.dataService.getMatchRoundStorage().delByPatternId(this.getPatternId());
        this.dataService.getVsGroupStorage().delByPatternId(this.getPatternId());
        this.dataService.getVsNodeStorage().delByPatternId(this.getPatternId());

        //删除选手对症
        //...

        this.dataService.getPatternStorage().saveState(this.getPatternId(), PatternStates.UnBuildSchedule);

        //回到数据库状态
        this.init();
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
    protected abstract MResult initSchedule(int players);

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
            if (Objects.equals(b.getRoundId(), roundId)) {
                groups.add(b);
            }
        });
        return groups;
    }

    public List<VsGroup> getVsGroups(int round) {
        MatchRound mr = this.getMatchRound(round);
        if (mr == null) {
            return new ArrayList<>();
        }
        return getVsGroups(mr.getId());
    }

    public List<VsNode> getVsNodes(String groupId) {
        List<VsNode> nodes = new ArrayList<>();
        this.nodes.forEach((a, b) -> {
            if (b.getGroupId().equals(groupId)) {
                nodes.add(b);
            }
        });
        return nodes;
    }

    public EnrollPlayer getPlayer(String playerId) {
        if (index == 0) {
            return getEnrollPlayer(playerId);
        }
        return null;
    }

    private EnrollPlayer getEnrollPlayer(String playerId) {
        return this.players.get(playerId);
    }

    /**
     * 装载报名选手
     *
     * @param players
     * @return
     */
    public MResult loadPlayers(Collection<EnrollPlayer> players) {
        for (EnrollPlayer p : players) {
            this.players.put(p.getPlayerId(), p);
        }
        this.schedulePlayers = this.players.size();
        return new MResult(MResult.SUCCESS_CODE, "成功");
    }

    /**
     * 分配选手进入赛程
     *
     * @return
     */
    public MResult assignPlayers() {
        Collection<EnrollPlayer> players = this.players.values();
        MResult result = dataService.getAssignStrategy().assign(this, players);
        if (result.getCode().equals(MResult.SUCCESS_CODE)) {
            for (EnrollPlayer p : players) {
                dataService.getEnrollPlayerStorage().savePlayerFirstNode(p.getPlayerId(), p.getMatchId(), p.getNodeId());
            }
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
        if (this.state != PatternStates.AssignedPlayers) {
            return false;
        }
        initEstablishVs();
        this.state = PatternStates.EstablishedVs;
        return true;
    }

    protected abstract void initEstablishVs();

    public boolean save() {
        //未确定真实人数时不保存赛程模型
//        if (this.players.size() == 0) {
//            return;
//        }

        boolean success;
        if (!this.first) {
            success = saveModify();
        } else {
            success = saveInit();
        }
        if (!success) {
            return false;
        }

        //保存模型状态
        success = dataService.getPatternStorage().saveState(this.getPatternId(), state);
        return success;
    }

    private boolean saveModify() {
        //保存模型
        try {
            for (MatchRound mr : this.rounds.values()) {
                if (mr.isModified()) {
                    boolean ok = dataService.getMatchRoundStorage().saveOrUpdate(mr);
                    if (!ok) {
                        return false;
                    }
                }
            }
            for (VsGroup group : this.groups.values()) {
                if (group.isModified()) {
                    boolean ok = dataService.getVsGroupStorage().saveOrUpdate(group);
                    if (!ok) {
                        return false;
                    }
                }
            }
            for (VsNode node : this.nodes.values()) {
                if (node.isModified()) {
                    boolean ok = dataService.getVsNodeStorage().saveOrUpdate(node);
                    if (!ok) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private boolean saveInit() {
        try {
            dataService.getMatchRoundStorage().batchSave(this.rounds.values());

            dataService.getVsGroupStorage().batchSave(this.groups.values());

            dataService.getVsNodeStorage().batchSave(this.nodes.values());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 轮次的晋级选手
     *
     * @param round
     * @return
     */
    public abstract Collection<EnrollPlayer> roundPromotionPlayers(int round);

    /**
     * 处理结果
     *
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
     *
     * @param playerId
     * @return
     */
    public abstract EnrollPlayer nextVsPlayer(String playerId);

    /**
     * 某论对阵列表
     *
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
    protected VsNode playerLastNode(String playerId) {
        MatchRound firstMr = this.getMatchRound(1);
        Collection<VsGroup> groups = this.getVsGroups(firstMr.getId());
        VsNode playerFirstNode = null;
        for (VsGroup group : groups) {
            for (VsNode node : this.getVsNodes(group.getId())) {
                if (node.getPlayerId().equals(playerId)) {
                    playerFirstNode = node;
                }
            }
            if (playerFirstNode != null) {
                break;
            }
        }
        return playerNextNode(playerFirstNode);
    }

    private VsNode playerNextNode(VsNode node) {
        if (node == null) {
            return null;
        }
        if (Strings.isNullOrEmpty(node.getPlayerId())) {
            return null;
        }
        VsNode nextNode = this.nodes.get(node.getWinNextId());
        if (Strings.isNullOrEmpty(nextNode.getPlayerId())) {
            return node;
        }
        return playerNextNode(nextNode);
    }

    /**
     * 位置互换
     *
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
        String player1id = vs1.getPlayerId();
        String player2id = vs2.getPlayerId();
        vs1.setPlayerId(player2id);
        vs1.modify();
        vs2.setPlayerId(player1id);
        vs2.modify();
        return new MResult(MResult.SUCCESS_CODE, "交换成功");
    }

    ///////////////////////////////////////////////////////////////////////
    //help method
    ///////////////////////////////////////////////////////////////////////

    protected VsGroup newVsGroup(short round, int index, String roundId, int nodes, boolean cache) {
        VsGroup group = new VsGroup();
        group.setId(MatchHelper.getItemId(this.cfg.getType(), PatternItemTypes.Group));
        group.setGroupPlayerCount(nodes);
        group.setRound(round);
        group.setIndex(index);
        group.setRoundId(roundId);
        group.setPatternId(this.cfg.getPatternId());
        group.setMatchId(this.cfg.getMatchId());
        group.modify();
        for (short i = 0; i < nodes; i++) {
            VsNode node = new VsNode();
            node.setId(MatchHelper.getItemId(this.cfg.getType(), PatternItemTypes.Node));
            node.setIndex(i);
            node.setPatternId(this.cfg.getPatternId());
            node.setGroupId(group.getId());
            node.setRound(round);
            node.setMatchId(this.cfg.getMatchId());
            node.modify();
            this.nodes.put(node.getId(), node);
        }
        if (cache) {
            this.groups.put(group.getId(), group);
        }
        return group;
    }

    protected boolean is2Pow(int c) {
        int i = 1;
        while (i != 0) {
            i = i << 1;
            if (i >= c) {
                break;
            }
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
//        Collection<VsGroup> groups = this.getVsGroups(mr.getPatternId());
//        for(VsGroup group:groups) {
//            VsNode emptyNode = null;
//            for(VsNode node:this.getVsNodes(group.getPatternId())){
//                if(node.getPlayerId() == 0 && !node.isBye()){
//                    emptyNode = node;
//                    break;
//                }
//            }
//            if(emptyNode != null) {
//                emptyNode.setInsert(true);
//                emptyNode.setPlayerId(srcNode.getPlayerId());
//                emptyNode.setSrcNodeId(srcNode.getPatternId());
//                emptyNode.modify();
//            }
//        }
//    }
}
