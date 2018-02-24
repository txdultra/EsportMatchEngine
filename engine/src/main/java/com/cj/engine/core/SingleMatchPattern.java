package com.cj.engine.core;

import com.cj.engine.core.cfg.BasePatternConfig;
import com.cj.engine.core.cfg.SinglePatternConfig;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 单淘赛模型
 * Created by tang on 2016/3/16.
 */
public class SingleMatchPattern extends AbstractMatchPattern {
    private boolean hasThrider;
    private boolean hasChampion;
    static final int GROUP_PLAYER_COUNTS = 2;

    @Override
    protected void initExt() {
    }

    public SingleMatchPattern(BasePatternConfig cfg, IDataService dataService) {
        super(cfg, dataService);
        this.initSetting();
    }

    private void initSetting() {
        if (getCfg().getGroupPlayerNumber() != GROUP_PLAYER_COUNTS) {
            throw new IllegalArgumentException("单淘赛制只支持小组人数为2");
        }
        SinglePatternConfig patternCfg = (SinglePatternConfig) getCfg();
        this.hasChampion = patternCfg.hasChampion();
        this.hasThrider = patternCfg.hasThrider();

        if (!this.hasChampion && this.hasThrider) {
            throw new IllegalArgumentException("需要决出第三名时必须决出冠军,参数has_champion");
        }
        if (!this.hasChampion && getCfg().getPromotions() <= 0) {
            throw new IllegalArgumentException("不决出冠军时必须设置晋级人数");
        }
    }

    protected int calculateRoundGroupQuantities(int counts) {
        int uppers = this.getCfg().getGroupPlayerNumber();
        while (counts > uppers) {
            uppers = uppers << 1;
        }
        return uppers / 2;
    }

    protected int calculateMaxRounds(int counts) {
        int maxRGs = calculateRoundGroupQuantities(counts);
        int rounds = 1;
        while (maxRGs > 1) {
            //到达此轮已完成本赛制晋级人员数量
            if (!this.hasChampion) {
                if (this.getCfg().getPromotions() >= maxRGs * this.getCfg().getGroupPlayerNumber()) {
                    break;
                }
            }
            rounds++;
            maxRGs = maxRGs / 2;
        }
        return rounds;
    }

    private void setNodesRel() {
        for (int i = 1; i <= this.rounds.size(); i++) {
            MatchRound cRound = this.getMatchRound(i);
            MatchRound nRound = this.getMatchRound(i + 1);
            //最后一轮
            if (nRound == null) {
                break;
            }
            List<VsGroup> cGroups = this.getVsGroups(cRound.getId());
            List<VsGroup> nGroups = this.getVsGroups(nRound.getId());
            for (int j = 0; j < cGroups.size(); j += 2) {
                VsGroup g1 = cGroups.get(j);
                VsGroup g2 = cGroups.get(j + 1);
                VsGroup ng = nGroups.get(j / 2);
                List<VsNode> nodes = this.getVsNodes(ng.getId());
                setGroupNodeWinRel(g1.getId(), nodes.get(0), true);
                setGroupNodeWinRel(g2.getId(), nodes.get(1), true);
            }
            if (this.hasThrider && (i - 1) == this.rounds.size()) {
                VsGroup g1 = cGroups.get(0);
                VsGroup g2 = cGroups.get(1);
                VsGroup n1 = nGroups.get(1);
                List<VsNode> nodes = this.getVsNodes(n1.getId());
                setGroupNodeWinRel(g1.getId(), nodes.get(0), false);
                setGroupNodeWinRel(g2.getId(), nodes.get(1), false);
            }
        }
    }


    private void setGroupNodeWinRel(String groupId, VsNode nextNode, boolean setWin) {
        Collection<VsNode> nodes = this.getVsNodes(groupId);
        for (VsNode node : nodes) {
            if (setWin) {
                node.setWinNextId(nextNode.getId());
            } else {
                node.setLoseNextId(nextNode.getId());
            }
            node.modify();
        }
    }

    @Override
    public MResult verifyNewPattern() {
        if (this.schedulePlayers < 4) {
            return new MResult("2001", "单赛程人数不能小于4");
        }
        return new MResult(MResult.SUCCESS_CODE, "允许");
    }

    @Override
    protected MResult initSchedule(int players) {
        if (players <= 0) {
            throw new IllegalArgumentException("参数不能小于等于0");
        }
        int maxRound = calculateMaxRounds(players);
        int maxRGs = calculateRoundGroupQuantities(players);
        int i = 1;
        while (maxRound >= i) {
            MatchRound mr = new MatchRound();
            mr.setRound(i);
            mr.setId(MatchHelper.getItemId(this.getCfg().getType(), PatternItemTypes.Round));
            mr.setMatchId(this.getCfg().getMatchId());
            mr.setPatternId(this.getPatternId());
            mr.setGroupCounts(maxRGs);
            for (int j = 0; j < maxRGs; j++) {
                VsGroup group = newVsGroup(i, j, mr.getId(), this.getCfg().getGroupPlayerNumber(), true);
                group.setPatternId(this.getPatternId());
            }
            //最后一轮
            if (maxRound == i && this.hasThrider) {
                VsGroup group = newVsGroup(i, 0, mr.getId(), this.getCfg().getGroupPlayerNumber(), true);
                group.setPatternId(this.getPatternId());
            }
            //加入到模型数据中
            this.rounds.put(mr.getId(), mr);
            //计算下一轮小组数
            maxRGs = maxRGs / this.getCfg().getGroupPlayerNumber();
            i++;
        }
        this.setNodesRel();
        this.maxRound = maxRound;
        return new MResult(MResult.SUCCESS_CODE, "创建成功");
    }

    @Override
    public Collection<EnrollPlayer> roundPromotionPlayers(int round) {
        Collection<EnrollPlayer> rps = new ArrayList<>();
        Collection<VsGroup> groups = this.getVsGroups(round);
        groups.forEach(a -> {
            rps.addAll(this.getVsNodes(a.getId()).stream().map(node -> this.getPlayer(node.getPlayerId())).collect(Collectors.toList()));
        });
        return rps;
    }

    @Override
    protected void assignedPlayersEvent() {
        Collection<VsGroup> groups = this.getVsGroups(1);
        for (VsGroup group : groups) {
            List<VsNode> nodes = this.getVsNodes(group.getId());
            int idx = -1;
            for (VsNode n : nodes) {
                if (n.getPlayerId() == 0) {
                    idx = n.getIndex();
                }
            }
            if (idx >= 0) {
                VsNode lNode = nodes.get(idx);
                idx ^= 1;
                VsNode cNode = nodes.get(idx);
                VsNode nextNode = this.nodes.get(cNode.getWinNextId());
                if (nextNode != null) {
                    nextNode.setPlayerId(cNode.getPlayerId());
                    nextNode.modify();
                }
                cNode.setState(VsNodeState.AutoPromoted);
                cNode.modify();
                //轮空
                lNode.setEmpty(true);
                lNode.setState(VsNodeState.UnPromoted);
                lNode.modify();
                //组状态
                group.setState(VsStates.Confirmed);
                group.modify();
                //进入下一阶段方法
                //this.gotoNextPattern(cNode.getPlayerId());
            }
        }
    }

    @Override
    protected void initEstablishVs() {
        for (int i = 1; i <= this.maxRound; i++) {
            for (VsGroup group : this.getVsGroups(i)) {
                buildVs(group.getId());
            }
        }
    }

    protected void buildVs(String groupId) {
        List<VsNode> nodes = this.getVsNodes(groupId);
        VsNode n1 = nodes.get(0);
        VsNode n2 = nodes.get(1);
        if (n1.getPlayerId() > 0 && n2.getPlayerId() > 0) {
            MatchVs vs = new MatchVs() {
            };
            vs.setLeftId(n1.getPlayerId());
            vs.setRightId(n2.getPlayerId());
            vs.setLeftNodeId(n1.getId());
            vs.setRightNodeId(n2.getId());
            vs.setGroupId(groupId);
            vs.setState(VsStates.UnDefined);
            dataService.getMatchVsService().save(vs, this.getCfg().getMatchId(), n1.getRound());
        }
    }

    @Override
    protected MResult unConfirmVs(MatchVs vs) {
        VsNode n1 = this.nodes.get(vs.getWinnerNodeId());
        VsNode n2 = this.nodes.get(vs.getLeftNodeId());
        if (n1 == null || n2 == null) {
            return new MResult("1102", "已到最后节点");
        }
        n1.setScore(vs.getWinnerScore());
        n2.setScore(vs.getLoserScore());
        n1.modify();
        n2.modify();
        dataService.getMatchVsService().save(vs, this.getCfg().getMatchId(), n1.getRound());
        return new MResult(MResult.SUCCESS_CODE, "成功更新");
    }

    @Override
    protected MResult confirmedVs(MatchVs vs) {
        VsNode node = this.nodes.get(vs.getWinnerNodeId());
        if (node == null) {
            return new MResult("1102", "节点不存在");
        }
        node.setScore(vs.getWinnerScore());
        node.setState(VsNodeState.Promoted);
        node.modify();

        List<VsNode> nodes = this.getVsNodes(node.getGroupId());
        nodes.stream().filter(n -> !n.getId().equals(node.getId())).forEach(n -> {
            n.setScore(vs.getLoserScore());
            n.setState(VsNodeState.UnPromoted);
            n.modify();
        });

        VsGroup group = this.groups.get(vs.getGroupId());
        group.setState(VsStates.Confirmed);
        group.modify();

        VsNode nextNode = this.nodes.get(node.getWinNextId());
        if (nextNode != null) {
            nextNode.setPlayerId(vs.getWinnerId());
            nextNode.modify();
            //生成下一轮对阵
            this.buildVs(nextNode.getGroupId());
        }
        dataService.getMatchVsService().save(vs, this.getCfg().getMatchId(), node.getRound());
        //进入下一阶段
        //this.gotoNextPattern(node.getPlayerId());

        return new MResult(MResult.SUCCESS_CODE, "成功更新");
    }

    @Override
    public EnrollPlayer nextVsPlayer(int playerId) {
        VsNode endNode = playerLastNode(playerId);
        if (endNode == null) {
            return null;
        }
        for (VsNode n : this.getVsNodes(endNode.getGroupId())) {
            if (!n.getId().equals(endNode.getId())) {
                return this.getPlayer(n.getPlayerId());
            }
        }
        return null;
    }

    @Override
    public Collection<MatchVs> getRoundVsList(int round, Map<String, Object> otherArgs) {
        Collection<VsGroup> groups = this.getVsGroups(round);
        return this.loadVsList(groups);
    }

    protected Collection<MatchVs> loadVsList(Collection<VsGroup> groups) {
        ArrayList<MatchVs> vss = new ArrayList<>();
        for (VsGroup group : groups) {
            List<VsNode> nodes = this.getVsNodes(group.getId());
            EnrollPlayer p1 = this.getPlayer(nodes.get(0).getPlayerId());
            EnrollPlayer p2 = this.getPlayer(nodes.get(1).getPlayerId());
            MatchVs vs = new MatchVs();
            vs.setLeftId(p1 != null ? p1.getPlayerId() : 0);
            vs.setRightId(p2 != null ? p2.getPlayerId() : 0);
            vs.setLeftNodeId(nodes.get(0).getId());
            vs.setRightNodeId(nodes.get(1).getId());
            vss.add(vs);
        }
        return vss;
    }

    @Override
    public MResult exchangeNodePlayer(String n1key, String n2key) {
        return super.exchangeNodePlayer(n1key, n2key);
    }

    /**
     * 可晋级列表
     *
     * @return
     */
    protected HashSet<String> getPromotionNodeIds() {
        HashSet<String> set = new HashSet<>();
        int round = 1;
        while (round <= this.maxRound) {
            Collection<VsGroup> groups = this.getVsGroups(round);
            if (groups.size() * this.getCfg().getGroupPlayerNumber() <= this.getCfg().getPromotions()) {
                for (VsGroup group : groups) {
                    Collection<VsNode> nodes = this.getVsNodes(group.getId());
                    nodes.forEach(a -> set.add(a.getId()));
                }
                break;
            }
            round++;
        }
        return set;
    }

//    /**
//     * 计算进入下一阶段
//     *
//     * @param playerId
//     */
//    @Override
//    public void gotoNextPattern(int playerId) {
//        if (this.getNextPattern() != null) {
//            VsNode lastNode = this.playerLastNode(playerId);
//            if (lastNode == null) return;
//            HashSet<String> pnIds = getPromotionNodeIds();
//            if (pnIds.contains(lastNode.getPatternId()))
//                this.getNextPattern().receivePrevPattern(lastNode);
//        }
//    }
}
