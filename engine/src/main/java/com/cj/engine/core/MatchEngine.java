package com.cj.engine.core;

import cn.neotv.match.engine.cfg.MPCfg;

import java.util.*;

/**
 * Created by tang on 2016/3/22.
 */
public class MatchEngine {
    private boolean inited;
    private int matchId;
    private IPatternDataProvider provider;
    private AbstractMatchPattern startPattern = null;
    private List<MPCfg> cfgs = new ArrayList<>();

    public MatchEngine(int matchId) {
        this.matchId = matchId;
    }

    public synchronized void init() {
        if(this.inited) return;
        cfgs = this.getCfgs();
        this.initPatterns(cfgs);
        this.inited = true;
    }

    private void initPatterns(Collection<MPCfg> cfgs) {
        List<AbstractMatchPattern> patterns = new ArrayList<>();
        for(MPCfg cfg:cfgs) {
            AbstractMatchPattern pattern = MatchHelper.newFactory(cfg);
            pattern.setDataProvider(this.provider);
            pattern.init();
            patterns.add(pattern);
        }
        //设置上下引用关系
        for(int i=0;i<patterns.size();i++) {
            AbstractMatchPattern cMr = patterns.get(i);
            AbstractMatchPattern nMr = null;
            if(i + 1 < patterns.size()) {
                nMr = patterns.get(i+1);
            }
            if(i == 0) {
                cMr.setNextPattern(nMr);
            }else{
                AbstractMatchPattern pMr = patterns.get(i-1);
                cMr.setNextPattern(nMr);
                cMr.setPreviousPattern(pMr);
            }
        }
        if(patterns.size() > 0)
            this.startPattern = patterns.get(0);
    }

    public synchronized void addPattern(MPCfg cfg) {
        for(MPCfg mpc:this.cfgs) {
            if(mpc.getPatternId().equals(cfg.getPatternId()))
                return;
        }
        this.cfgs.add(cfg);
    }

    public synchronized void saveCfgs() {
        int pid = 0;
        for(MPCfg cfg:this.cfgs) {
            cfg.setPid(pid);
            this.provider.saveMPCfg(cfg);
            pid = cfg.getId();
        }
    }

    public synchronized MResult removePattern(String patternId) {
        PatternStates state = this.provider.getPatternState(patternId);
        if(state != PatternStates.UnBuildSchedule) {
            return new MResult("1102","赛程已构建,不能删除");
        }
        MPCfg cfg = null;
        for(MPCfg _cfg:this.cfgs) {
            if(_cfg.getPatternId().equals(patternId)) {
                cfg = _cfg;
                break;
            }
        }
        if(cfg != null) {
            this.cfgs.remove(cfg);
            this.initPatterns(this.cfgs);
            return new MResult(MResult.SUCCESS_CODE,"删除成功");
        }
        return new MResult("1103","配置不存在");
    }

    public List<MPCfg> getCfgs() {
        List<MPCfg> cfgs = new ArrayList<>();
        MPCfg cfg = this.provider.getMPCfg(this.matchId,0);
        if(cfg == null)
            return cfgs;
        cfgs.add(cfg);
        for(;;){
            cfg = this.provider.getMPCfg(this.matchId,cfg.getId());
            if(cfg != null)
                cfgs.add(cfg);
            else
                break;
        }
        return cfgs;
    }

    public void setDataProvider(IPatternDataProvider provider) {
        this.provider = provider;
    }

    public MResult buildSchedule(int maxPlayers) {
        this.initPatterns(this.cfgs);
        AbstractMatchPattern fMp = this.startPattern;
        if(fMp != null) {
            fMp.init();
            return fMp.buildSchedule(maxPlayers);
        }
        return new MResult("1100","没有可供构建的赛程");
    }

    public synchronized void save() {
        AbstractMatchPattern fMp = this.startPattern;
        while(fMp != null) {
            fMp.save();
            fMp = fMp.getNextPattern();
        }
    }

    public AbstractMatchPattern getPattern(String patternId) {
        AbstractMatchPattern fMp = this.startPattern;
        while(fMp != null) {
            if(fMp.getPatternId().equals(patternId))
                return fMp;
            fMp = fMp.getNextPattern();
        }
        return null;
    }

    public Collection<AbstractMatchPattern> getPatterns() {
        List<AbstractMatchPattern> mps = new ArrayList<>();
        AbstractMatchPattern mp = this.startPattern;
        for(;;){
            if(mp != null) {
                mps.add(mp);
                mp = mp.getNextPattern();
            }else{
                break;
            }
        }
        return mps;
    }
}
