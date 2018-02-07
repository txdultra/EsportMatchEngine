package com.cj.engine.core;

import cn.neotv.match.engine.cfg.GroupMPCfg;
import cn.neotv.match.engine.cfg.MPCfg;

import java.util.*;

/**
 * Created by tang on 2016/3/16.
 */
public class GroupMatchPattern extends AbstractMatchPattern {
    private GroupMatchPatternScoringTypes scoringType = GroupMatchPatternScoringTypes.BigSocre;
    private double bigWinScore;
    private double bigLoseScore;
    private double smallWinScore;
    private double smallLoseScore;
    private Map<String,Collection<MatchVs>> groupVss = new LinkedHashMap<>();

    public GroupMatchPatternScoringTypes getScoringType() {
        return this.scoringType;
    }
    /**
     * 定义积分方式
     * @param scoringType
     */
    public void setScoringType(GroupMatchPatternScoringTypes scoringType) {
        this.scoringType = scoringType;
    }

    @Override
    protected void initExtend()  {
        //装载对阵结构
        MatchRound mr = this.getMatchRound(1);
        if(mr != null) {
            Collection<VsGroup> groups = this.getVsGroups(mr.getId());
            for (VsGroup group : groups) {
                Collection<MatchVs> vss = this.dataProvider.getVss(group.getId());
                this.groupVss.put(group.getId(), vss);
            }
        }
    }

    public GroupMatchPattern(MPCfg cfg) {
        super(cfg);
        if(!(cfg instanceof GroupMPCfg))
            throw new IllegalArgumentException("小组赛制配置对象类型必须是GroupMPCfg");
        if(cfg.getPromotionCounts() <= 0)
            throw new IllegalArgumentException("小组赛制必须设置晋级人数");
        if(cfg.getPromotionCounts() >= cfg.getGroupPlayerCount())
            throw new IllegalArgumentException("小组赛制每组晋级人数不能大于等于小组人数");
        GroupMPCfg gCfg = (GroupMPCfg)cfg;
        this.bigWinScore = gCfg.getbWinScore();
        this.bigLoseScore = gCfg.getbLoseScore();
        this.smallWinScore = gCfg.getsWinScore();
        this.smallLoseScore = gCfg.getsLoseScore();
    }

    @Override
    protected synchronized void reset() {
        this.groupVss.clear();
        super.reset();
    }

    @Override
    public MResult verifyNewPattern() {
        if(this.schedulePlayers < 3)
            return new MResult("2001","小组赛程人数不能小于4");
        return new MResult(MResult.SUCCESS_CODE,"允许");
    }

    @Override
    protected MResult newSchedule(int counts) {
        int gcs = 0;
        if (counts % this.cfg.getGroupPlayerCount() == 0) {
            gcs = counts / this.cfg.getGroupPlayerCount();
        } else {
            gcs = (counts / this.cfg.getGroupPlayerCount()) + 1;
        }
        MatchRound mr = new MatchRound();
        mr.setRound(1);
        mr.setId(MatchHelper.getItemId(this.cfg.getPatternType(), PatternItemTypes.Round));
        mr.setMatchId(this.cfg.getMatchId());
        mr.setPatternId(this.getPatternId());
        mr.setGroupCounts(gcs);
        mr.modify();
        //加入到模型数据中
        this.rounds.put(mr.getId(), mr);
        int i = 0;
        int tmp = counts;
        while (i < gcs) {
            int gc = 0;
            if(tmp > this.cfg.getGroupPlayerCount())
                gc = this.cfg.getGroupPlayerCount();
            else
                gc = tmp;
            tmp -= this.cfg.getGroupPlayerCount();
            VsGroup group = this.newVsGroup(1, i, mr.getId(), gc, true);
            group.setPatternId(mr.getPatternId());
            i++;
        }

        return new MResult(MResult.SUCCESS_CODE, "创建成功");
    }

    @Override
    public int promotionNextPatternPlayerCounts() {
        int promotions = 0;
        Collection<VsGroup> groups = this.getVsGroups(1);
        for(VsGroup group:groups) {
            Collection<VsNode> nodes = this.getVsNodes(group.getId());
            if(nodes.size() >= this.cfg.getPromotionCounts())
                promotions += this.cfg.getPromotionCounts();
            else
                promotions += nodes.size();
        }
        return promotions;
    }

    @Override
    protected VsGroup newVsGroup(int round,int index, String roundId, int nodes,boolean cache) {
        VsGroup group = new VsGroup(MatchHelper.getItemId(this.cfg.getPatternType(),PatternItemTypes.Group),nodes);
        group.setCurrentRound(round);
        group.setIndex(index);
        group.setRoundId(roundId);
        group.setPatternId(this.cfg.getPatternId());
        group.modify();
        for(int i=0;i<nodes;i++) {
            VsNode node = new GroupVsNode(MatchHelper.getItemId(this.cfg.getPatternType(),PatternItemTypes.Node));
            node.setIndex(i);
            node.setPatternId(this.cfg.getPatternId());
            node.setGroupId(group.getId());
            node.modify();
            this.nodes.put(node.getId(),node);
        }
        if(cache)
            this.groups.put(group.getId(),group);
        return group;
    }

    @Override
    protected void assignedPlayersEvent() {
        Collection<VsGroup> groups = this.getVsGroups(1);
        for(VsGroup group:groups) {
            int count = 0;
            Collection<VsNode> nodes = getVsNodes(group.getId());
            for(VsNode n:nodes) {
                if(n.getPlayerId() > 0)
                    count++;
                else {
                    n.setBye(true);
                    n.modify();
                }
            }
            if(count <= this.cfg.getPromotionCounts()){
                for(VsNode n:nodes){
                    if(n.getPlayerId() > 0){
                        n.setState(VsNodeState.AutoPromoted);
                        //进入下一阶段
                        this.gotoNextPattern(n.getPlayerId());
                    }
                }
                group.setState(VsStates.Confirmed);
                group.modify();
            }
        }
    }

    @Override
    protected void initEstablishVs() {
        Collection<VsGroup> groups = this.getVsGroups(1);
        for(VsGroup group:groups) {
            if(group.getState() == VsStates.Confirmed)
                continue;
            int gSize = groups.size();
            List<MatchVs> vss = new ArrayList<>();
            for(int i=0;i<gSize;i++) {
                for(int j=i+1;j<gSize;j++){
                    List<VsNode> nodes = this.getVsNodes(group.getId());
                    VsNode n1 = nodes.get(i);
                    VsNode n2 = nodes.get(j);
                    if(n1.getPlayerId() == n2.getPlayerId())
                        continue;

                    MatchVs vs = new MatchVs(){};
                    vs.setLeftId(n1.getPlayerId());
                    vs.setRightId(n2.getPlayerId());
                    vs.setLeftNodeId(n1.getId());
                    vs.setRightNodeId(n2.getId());
                    vs.setGroupId(group.getId());
                    vs.setState(VsStates.UnConfirm);
                    this.dataProvider.saveMatchVs(vs,this.cfg.getMatchId(),n1.getRound());
                    vss.add(vs);
                }
            }
            this.groupVss.put(group.getId(),vss);
        }
    }

    @Override
    public Collection<EnrollPlayer> roundPromotionPlayers(int round) {
        Collection<VsGroup> groups = this.getVsGroups(round);
        ArrayList<EnrollPlayer> rps = new ArrayList<>();
        for(VsGroup group:groups) {
            List<VsNode> nodes = this.getVsNodes(group.getId());
            sortNodesByScore(nodes);
            for(int i=0;i<this.cfg.getPromotionCounts() && i < nodes.size();i++){
                rps.add(this.getPlayer(nodes.get(i).getPlayerId()));
            }
        }
        return rps;
    }

    private void sortNodesByScore(List<VsNode> nodes) {
        Collections.sort(nodes, (n1, n2) -> {
            if(n1.getScore() > n2.getScore())
                return 1;
            if(n1.getScore() == n2.getScore())
                return 0;
            return -1;
        });
    }

    @Override
    protected MResult unConfirmVs(MatchVs vs) {
        this.dataProvider.saveMatchVs(vs,this.cfg.getMatchId(),1);
        return new MResult(MResult.SUCCESS_CODE,"成功更新");
    }

    @Override
    protected MResult confirmedVs(MatchVs vs) {
        vs.setVerdictTime((int)(System.currentTimeMillis() / 1000));
        this.dataProvider.saveMatchVs(vs,this.cfg.getMatchId(),1);

        GroupVsNode lNode = (GroupVsNode)this.nodes.get(vs.getLeftNodeId());
        GroupVsNode wNode = (GroupVsNode)this.nodes.get(vs.getRightNodeId());
        if(vs.getWinnerNodeId() == lNode.getId()) {
            lNode.addWins(1);
            wNode.addLoses(1);
            setNodeScores(lNode,vs.getWinnerScore(),true);
            setNodeScores(wNode,vs.getLoserScore(),false);
        }else if(vs.getWinnerNodeId() == wNode.getId()) {
            wNode.addWins(1);
            lNode.addLoses(1);
            setNodeScores(lNode,vs.getLoserScore(),false);
            setNodeScores(wNode,vs.getWinnerScore(),true);
        }else{
            wNode.addPings(1);
            lNode.addPings(1);
        }
        lNode.modify();
        wNode.modify();
        this.dataProvider.saveMatchVs(vs,this.cfg.getMatchId(),1);
        this.setGroupPromotion(vs.getGroupId());
        //进入下一阶段
        this.gotoNextPattern(lNode.getPlayerId());
        this.gotoNextPattern(wNode.getPlayerId());
        return new MResult(MResult.SUCCESS_CODE,"成功更新");
    }

    private void setGroupPromotion(String groupId) {
        Collection<MatchVs> vss = this.groupVss.get(groupId);
        for(MatchVs vs:vss) {
            if(vs.getState() != VsStates.Confirmed)
                return;
        }
        List<VsNode> rankNodes = this.groupRankNodes(groupId);
        for(int i=0;i<this.cfg.getPromotionCounts() && i < rankNodes.size();i++) {
            VsNode n = rankNodes.get(i);
            if(n.getPlayerId() > 0){
                n.setState(VsNodeState.Promoted);
                n.modify();
            }
        }
        VsGroup group = this.groups.get(groupId);
        if(group != null){
            group.setState(VsStates.Confirmed);
            group.modify();
        }
    }

    /**
     * 获取当前小组排名
     * @param groupId
     * @return
     */
    private List<VsNode> groupRankNodes(String groupId) {
        List<VsNode> nodes = this.getVsNodes(groupId);
        sortNodesByScore(nodes);
        return nodes;
    }

    private void setNodeScores(GroupVsNode node,int score,boolean isWinner) {
        double integral = 0;
        switch (this.scoringType) {
            case SmallScore:
                integral += score;
                break;
            case BigSocre:
                if(isWinner)
                    integral += 1;
                break;
            case Mixture:
                if(isWinner) {
                    integral += this.bigWinScore + (this.smallWinScore * score);
                }else {
                    integral += this.bigLoseScore + (this.smallLoseScore * score);
                }
                break;
        }
        double oScore = node.getScore();
        node.setScore(oScore + integral);
    }

    @Override
    public EnrollPlayer nextVsPlayer(int playerId) {
        VsGroup playerOfGroup = findPlayerInGroup(playerId);
        if(playerOfGroup == null)
            return null;
        Collection<MatchVs> vss = this.groupVss.get(playerOfGroup.getId());
        for(MatchVs vs:vss) {
            if(vs.getState() != VsStates.Confirmed){
                if(vs.getLeftId() == playerId)
                    return this.getPlayer(vs.getRightId());
                if(vs.getRightId() == playerId)
                    return this.getPlayer(vs.getLeftId());
            }
        }
        return null;
    }

    private VsGroup findPlayerInGroup(int playerId) {
        Collection<VsGroup> groups = this.getVsGroups(1);
        VsGroup playerOfGroup = null;
        for(VsGroup group:groups) {
            Collection<VsNode> nodes = this.getVsNodes(group.getId());
            for(VsNode node:nodes) {
                if(node.getPlayerId() == playerId)
                    playerOfGroup = group;
            }
            if(playerOfGroup != null)
                break;
        }
        return playerOfGroup;
    }

    @Override
    public Collection<MatchVs> getRoundVsList(int round,Map<String,Object> otherArgs){
        if(otherArgs == null || !otherArgs.containsKey("group_id"))
            return new ArrayList<>();;
        String groupId = (String)otherArgs.get("group_id");
        return this.groupVss.get(groupId);
    }

    @Override
    public MResult exchangeNodePlayer(String n1key, String n2key) {
        return super.exchangeNodePlayer(n1key,n2key);
    }

    @Override
    public void gotoNextPattern(int playerId) {
        if(this.getNextPattern() == null)
            return;
        VsGroup playerOfGroup = findPlayerInGroup(playerId);
        if(playerOfGroup == null)
            return;
        Collection<VsNode> nodes = this.getVsNodes(playerOfGroup.getId());
        for(VsNode n:nodes) {
            if(n.getState() == VsNodeState.Promoted
                    || n.getState() == VsNodeState.AutoPromoted) {
                this.getNextPattern().receivePrevPattern(n);
            }
        }
    }
}