package com.cj.engine.core;

import cn.neotv.match.engine.cfg.DoubleMPCfg;
import cn.neotv.match.engine.cfg.MPCfg;
import java.util.*;

/**
 * Created by tang on 2016/3/16.
 */

public class DoubleMatchPattern extends SingleMatchPattern {

    private Map<String,MatchRound> loseRounds = new LinkedHashMap<>();
    private Map<String,VsGroup> loseGroups = new LinkedHashMap<>();

    public DoubleMatchPattern(MPCfg cfg) {
        super(cfg);
        if(!(cfg instanceof DoubleMPCfg))
            throw new IllegalArgumentException("双败赛制配置对象类型必须是DoubleMPCfg");
        if(cfg.getGroupPlayerCount() != 2)
            throw new IllegalArgumentException("双败赛制只支持小组人数为2");
    }

    @Override
    protected void initExtend()  {
        //装载模型结构
        Collection<MatchRound> rounds = dataProvider.getMatchRounds(cfg.getPatternId(),1);
        for(MatchRound round:rounds) {
            this.loseRounds.put(round.getId(),round);
        }
        Collection<VsGroup> groups = dataProvider.getGroups(cfg.getPatternId(),1);
        for(VsGroup group:groups) {
            this.loseGroups.put(group.getId(),group);
        }
        this.initLoseCacheData();
    }

    private void initLoseCacheData() {
        if(cache == null) {
            return;
        }
        cache.multiAddMatchRounds(this.getPatternId(), this.loseRounds.values());
        for(MatchRound mr:this.loseRounds.values()) {
            Collection<VsGroup> groups = getLoseVsGroups(mr.getId());
            cache.multiAddVsGroups(mr.getId(), groups);
            for(VsGroup group:groups) {
                Collection<VsNode> nodes = getVsNodes(group.getId());
                cache.multiAddVsNodes(group.getId(), nodes);
            }
        }
    }

    @Override
    public void save() {
        super.save();
        for(MatchRound mr:this.loseRounds.values()) {
            if(mr.isModified())
                this.dataProvider.saveRound(mr);
        }
        Collection<VsGroup> groups = new ArrayList<>();
        for(VsGroup group:this.loseGroups.values()) {
            if(group.isModified())
                groups.add(group);
        }
        this.dataProvider.batchSaveVsGroups(groups);
    }

    @Override
    protected int calculateMaxRounds(int counts) {
        int maxRGs = calculateRoundGroupQuantities(counts);
        int rounds = 1;
        while(maxRGs > 1) {
            //到达此轮已完成本赛制晋级人员数量
            if(this.cfg.getPromotionCounts() > 0) {
                if(this.cfg.getPromotionCounts() / 2 >= maxRGs * this.cfg.getGroupPlayerCount()) {
                    break;
                }
            }
            rounds++;
            maxRGs = maxRGs / 2;
        }
        return rounds;
    }

    private int calculateLoseRoundGroupQuantities(int counts) {
        int c = counts/2;
        int uppers = this.cfg.getGroupPlayerCount();
        while(c > uppers) {
            uppers = uppers << 1;
        }
        return uppers / 2;
    }

    private int calculateLoseMaxRounds(int counts) {
        //有败者组人数不能小于4人
        if(counts < 2)
            return 0;
        int rounds = 1;
        int maxRGs = calculateLoseRoundGroupQuantities(counts);
        while(maxRGs > 1){
            if(this.cfg.getPromotionCounts() > 0) {
                if(this.cfg.getPromotionCounts() / 2 >= maxRGs * this.cfg.getGroupPlayerCount()) {
                    break;
                }
            }
            if(rounds % 2 == 0) {
                maxRGs = maxRGs / 2;
            }
            rounds++;
        }
        if(this.cfg.getPromotionCounts() > 0) {
            return rounds;
        }
        return rounds + 1;
    }

    @Override
    protected synchronized void reset() {
        this.loseRounds.clear();
        this.loseGroups.clear();
        super.reset();
    }


    @Override
    public MResult verifyNewPattern() {
        if(this.schedulePlayers < 4)
            return new MResult("2001","双败赛程人数不能小于4");
        if(this.cfg.getPromotionCounts() > 0 && !this.is2Pow(this.cfg.getPromotionCounts()))
            return new MResult("2002","双败赛程晋级人数必须为2的整数幂");
        return new MResult(MResult.SUCCESS_CODE,"允许");
    }

    @Override
    protected MResult newSchedule(int counts) {
        if(counts <= 0)
            throw new IllegalArgumentException("参数不能小于等于0");
        //先构建胜者组模型
        super.newSchedule(counts);
        //实现败者组模型
        int maxRound = calculateLoseMaxRounds(counts);
        int maxRGs = calculateLoseRoundGroupQuantities(counts);
        int i = 1;
        while(maxRound >= i) {
            MatchRound mr = new MatchRound();
            mr.setCategory(1); //败者组赛程
            mr.setRound(i);
            mr.setId(MatchHelper.getItemId(this.cfg.getPatternType(),PatternItemTypes.Round));
            mr.setMatchId(this.cfg.getMatchId());
            mr.setPatternId(this.getPatternId());
            mr.setGroupCounts(maxRGs);
            mr.modify();
            for(int j = 0;j<maxRGs;j++) {
                VsGroup group = this.newVsGroup(i,j,mr.getId(),this.cfg.getGroupPlayerCount(),false);
                if(i % 2 == 0) {
                    List<VsNode> nodes = this.getVsNodes(group.getId());
                    nodes.get(0).setInsert(true);
                }
                group.setCurrentRound(i);
                group.setCategory(1);//败者组赛程
                this.loseGroups.put(group.getId(),group);
            }
            //加入到模型数据中
            this.loseRounds.put(mr.getId(),mr);

            //计算下一轮小组数,i % 2 == 0 和前轮group数一样
            if(i % 2 == 0 && i != 1) {
                maxRGs = maxRGs / 2;
            }
            i++;
        }
        //胜者组最后多加一轮
        MatchRound gjMr = new MatchRound();
        gjMr.setRound(this.maxRound+1);
        gjMr.setId(MatchHelper.getItemId(this.cfg.getPatternType(),PatternItemTypes.Round));
        gjMr.setMatchId(this.cfg.getMatchId());
        gjMr.setPatternId(this.getPatternId());
        gjMr.setGroupCounts(1);
        gjMr.modify();
        this.rounds.put(gjMr.getId(), gjMr);
        VsGroup last = this.newVsGroup(this.maxRound + 1,0,gjMr.getId(),this.cfg.getGroupPlayerCount(),true);
        this.loseGroups.put(last.getId(),last);
        this.maxRound++;

        this.setNodesRel();

        return new MResult(MResult.SUCCESS_CODE,"创建成功");
    }

    private void setNodesRel() {
        this.setWinLastNodesRel();
        this.setWinLoserNodesRel();
        this.setLoseScheduleNodesRel();
    }

    private void setWinLastNodesRel() {
        MatchRound winLastSecondMr = this.getMatchRound(this.maxRound - 1);
        MatchRound winLastMr = this.getMatchRound(this.maxRound);
        MatchRound loseLastMr = this.getLoseMatchRound(this.loseRounds.size());

        VsGroup winLastGroup = this.getVsGroups(winLastMr.getId()).get(0);
        List<VsNode> winLastNodes = this.getVsNodes(winLastGroup.getId());
        //设置败者组最后一轮
        VsGroup loseGroup = this.getLoseVsGroups(loseLastMr.getId()).get(0);
        this.getVsNodes(loseGroup.getId()).forEach(a->{
            a.setWinNextId(winLastNodes.get(1).getId());
            a.modify();
        });
        //胜者组最后第二轮
        VsGroup winLastSecondGroup = this.getVsGroups(winLastSecondMr.getId()).get(0);
        this.getVsNodes(winLastSecondGroup.getId()).forEach(a->{
            a.setWinNextId(winLastNodes.get(0).getId());
            a.modify();
        });
    }

    private void setWinLoserNodesRel() {
        //第一轮
        this.setLoseFirstRoundNodesRel();
        //第二轮
        int loseIdx = 2;
        for(int i=2;i<=this.maxRound - 1;i++) {
            MatchRound winMr = this.getMatchRound(i);
            MatchRound loseMr = this.getLoseMatchRound(loseIdx);
            if(loseMr == null) return;
            List<VsGroup> winGroups = this.getVsGroups(winMr.getId());
            List<VsGroup> loseGroups = this.getLoseVsGroups(loseMr.getId());
            for(int j =0;j<winGroups.size();j++) {
                VsGroup wg1 = winGroups.get(j);
                VsGroup lg1 = loseGroups.get(j);
                List<VsNode> wNodes = this.getVsNodes(wg1.getId());
                List<VsNode> lNodes = this.getVsNodes(lg1.getId());
                wNodes.forEach(a->{
                    a.setLoseNextId(lNodes.get(0).getId());
                    a.modify();
                });
            }
            loseIdx +=2;
        }
    }

    private void setLoseFirstRoundNodesRel() {
        MatchRound mr = this.getMatchRound(1);
        MatchRound loseMr = this.getLoseMatchRound(1);
        List<VsGroup> wGroups = this.getVsGroups(mr.getId());
        List<VsGroup> lGroups = this.getLoseVsGroups(loseMr.getId());
        for(int j=0;j<wGroups.size();j+=2) {
            VsGroup wg1 = wGroups.get(j);
            VsGroup wg2 = wGroups.get(j+1);
            VsGroup lg = lGroups.get(j/2);
            List<VsNode> nodes = this.getVsNodes(lg.getId());
            setLoseNodeId(wg1,nodes.get(0));
            setLoseNodeId(wg2,nodes.get(1));
            nodes.forEach(a->a.setInsert(true));
        }
    }

    private void setLoseNodeId(VsGroup group,VsNode nextLoseNode) {
        List<VsNode> nodes = this.getVsNodes(group.getId());
        for(VsNode node:nodes) {
            node.setLoseNextId(nextLoseNode.getId());
            node.modify();
        }
    }

    private void setLoseScheduleNodesRel() {
       int loseRounds = this.loseRounds.size();
       for(int i=1;i<=loseRounds;i++) {
           MatchRound pMr = this.getLoseMatchRound(i);
           MatchRound nMr = this.getLoseMatchRound(i+1);

           //最后一轮
           if(i==loseRounds) {
               //winner 对应胜者组最后一轮
               MatchRound winLastMr = this.getMatchRound(this.maxRound);
               List<VsGroup> winGroups = this.getVsGroups(winLastMr.getId());
               VsNode winNode = this.getVsNodes(winGroups.get(0).getId()).get(1);
               VsGroup loseGroup = this.getLoseVsGroups(pMr.getId()).get(0);
               this.getVsNodes(loseGroup.getId()).forEach(a->{
                   a.setWinNextId(winNode.getId());
                   a.modify();
               });
               break;
           }

           List<VsGroup> pGroups = this.getLoseVsGroups(pMr.getId());
           List<VsGroup> nGroups = this.getLoseVsGroups(nMr.getId());
           if(pGroups.size() == nGroups.size()) {
               for(int j=0;j<pGroups.size();j++) {
                   VsGroup g1 = pGroups.get(j);
                   VsGroup n1 = nGroups.get(j);
                   List<VsNode> nNodes = this.getVsNodes(n1.getId());
                   this.getVsNodes(g1.getId()).forEach(a->{
                       a.setWinNextId(nNodes.get(1).getId());
                       a.modify();
                   });
               }
           }else{
               for(int j=0;j<pGroups.size();j+=2) {
                   VsGroup g1 = pGroups.get(j);
                   VsGroup g2 = pGroups.get(j+1);
                   VsGroup n1 = nGroups.get(j / 2);
                   List<VsNode> g1Nodes = this.getVsNodes(g1.getId());
                   List<VsNode> g2Nodes = this.getVsNodes(g2.getId());
                   List<VsNode> nNodes = this.getVsNodes(n1.getId());
                   g1Nodes.forEach(a->{
                       a.setWinNextId(nNodes.get(0).getId());
                       a.modify();
                   });
                   g2Nodes.forEach(a->{
                       a.setWinNextId(nNodes.get(1).getId());
                       a.modify();
                   });
               }
           }
       }
    }

    private List<VsGroup> getLoseVsGroups(String roundId) {
        List<VsGroup> groups = new ArrayList<>();
        this.loseGroups.forEach((a,b) -> {
            if(b.getRoundId().equals(roundId))
                groups.add(b);
        });
        return groups;
    }

    private MatchRound getLoseMatchRound(int round) {
        for(MatchRound mr:this.loseRounds.values()){
            if(mr.getRound() == round)
                return mr;
        }
        return null;
    }

    @Override
    public List<MatchSchedule> getSchedules() {
        List<MatchSchedule> mss = super.getSchedules();
        MatchSchedule loseWs = new MatchSchedule();
        loseWs.addAll(this.loseRounds.values());
        mss.add(loseWs);
        return mss;
    }

    @Override
    public Collection<EnrollPlayer> roundPromotionPlayers(int round) {
        return super.roundPromotionPlayers(round);
    }

    @Override
    protected void assignedPlayersEvent() {
        //胜者组事件
        super.assignedPlayersEvent();
        //胜者组第一轮
        Collection<VsGroup> wGroups = this.getVsGroups(1);
        for(VsGroup group:wGroups) {
            List<VsNode> nodes = this.getVsNodes(group.getId());
            int idx = -1;
            for(VsNode n:nodes) {
                if(n.getState() == VsNodeState.AutoPromoted) {
                    idx = n.getIndex();
                    //进入下一阶段
                    this.gotoNextPattern(n.getPlayerId());
                }
            }
            if(idx >= 0) {
                idx ^= 1;
                VsNode node = nodes.get(idx);
                VsNode lNode = this.nodes.get(node.getLoseNextId());
                lNode.setBye(true);
                lNode.setState(VsNodeState.UnPromoted);
                lNode.modify();

                group.setState(VsStates.Confirmed);
                group.modify();
            }
        }
        //败者组
        for(int round=0;round<this.loseRounds.size();round++) {
            Collection<VsGroup> groups = this.getVsGroups(round);
            for(VsGroup g:groups){
                List<VsNode> nodes = this.getVsNodes(g.getId());
                VsNode n1 = nodes.get(0);
                VsNode n2 = nodes.get(1);
                //两个都是轮空
                if(n1.isBye() && n2.isBye()) {
                    VsNode nextNode = this.nodes.get(n1.getWinNextId());
                    nextNode.setBye(true);
                    nextNode.setState(VsNodeState.UnPromoted);
                    nextNode.modify();

                    g.setState(VsStates.Confirmed);
                    g.modify();
                }else if(n1.isBye() || n2.isBye()){
                    //一个轮空
                    int idx = -1;
                    if(n1.isBye())
                        idx = n2.getIndex();
                    if(n2.isBye())
                        idx = n1.getIndex();
                    VsNode n = nodes.get(idx);
                    if(n.getPlayerId() > 0){
                        VsNode nextNode = this.nodes.get(n.getWinNextId());
                        nextNode.setPlayerId(n.getPlayerId());
                        nextNode.modify();
                        //进入下一阶段
                        this.gotoNextPattern(n.getPlayerId());
                    }
                    idx ^= 1;
                    VsNode ln = nodes.get(idx);
                    ln.setState(VsNodeState.UnPromoted);
                    ln.modify();

                    g.setState(VsStates.Confirmed);
                    g.modify();
                }
            }
        }
    }

    @Override
    protected void initEstablishVs(){
        //胜者组
        super.initEstablishVs();
        //败者组
        for(int i=1;i<=this.loseRounds.size();i++) {
            MatchRound mr = this.getLoseMatchRound(i);
            for(VsGroup group:this.getLoseVsGroups(mr.getId())){
                super.buildVs(group.getId());
            }
        }
    }

    @Override
    public EnrollPlayer nextVsPlayer(int playerId) {
        return super.nextVsPlayer(playerId);
    }

    @Override
    public Collection<MatchVs> getRoundVsList(int round,Map<String,Object> otherArgs){
        if(otherArgs == null || !otherArgs.containsKey("is_wingroup"))
            return new ArrayList<>();
        boolean isWgroup = (Boolean) otherArgs.get("is_wingroup");
        if(isWgroup) {
            return super.getRoundVsList(round,null);
        }
        MatchRound loseMr = this.getLoseMatchRound(round);
        if(loseMr == null)
            return new ArrayList<>();
        Collection<VsGroup> groups = this.getLoseVsGroups(loseMr.getId());
        return loadVsList(groups);
    }


    @Override
    public MResult exchangeNodePlayer(String n1key, String n2key) {
        return super.exchangeNodePlayer(n1key,n2key);
    }

    @Override
    protected HashSet<String> getPromotionNodeIds(){
        HashSet<String> set = new HashSet<>();
        int wRound = 1;
        int lRound = -1;
        while(wRound <= this.maxRound) {
            MatchRound mr = this.getMatchRound(wRound);
            List<VsGroup> groups = this.getVsGroups(mr.getId());
            //第一轮
            if(groups.size() * this.cfg.getGroupPlayerCount() < this.cfg.getPromotionCounts()) {
                break;
            }
            //后面几轮
            if(groups.size() * this.cfg.getGroupPlayerCount() == this.cfg.getPromotionCounts()){
                List<VsNode> tempNode = this.getVsNodes(groups.get(0).getId());
                String loseNextId = this.getVsNodes(tempNode.get(0).getGroupId()).get(0).getLoseNextId();
                VsNode loseNode = this.nodes.get(loseNextId);
                wRound++;
                lRound = loseNode.getRound() + 1;
                break;
            }
            wRound++;
        }
        //胜者组加入晋级node
        MatchRound wMr = this.getMatchRound(wRound);
        Collection<VsGroup> wGroups = this.getVsGroups(wMr.getId());
        for(VsGroup group:wGroups) {
            this.getVsNodes(group.getId()).forEach(a->set.add(a.getId()));
        }
        //败者组
        if(lRound >= 1) {
            MatchRound lWr = this.getLoseMatchRound(lRound);
            Collection<VsGroup> lGroups = this.getLoseVsGroups(lWr.getId());
            for(VsGroup group:lGroups) {
                this.getVsNodes(group.getId()).forEach(a->set.add(a.getId()));
            }
        }
        return set;
    }
}
