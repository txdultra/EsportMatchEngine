package com.cj.engine.core;

import com.cj.engine.core.cfg.BasePatternConfig;
import com.cj.engine.model.MatchInfo;
import com.cj.engine.storage.IMatchStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * Created by tang on 2016/3/22.
 */
@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MatchEngine implements IMatchEngine {
    private boolean initialized;
    private int matchId;
    private MatchInfo match;
    private AbstractMatchPattern startPattern = null;
    private List<BasePatternConfig> cfgs = new ArrayList<>();

    @Autowired
    private IMatchStorage matchService;

    @Autowired
    private PatternConfigLoader cfgLoader;

    public MatchEngine(int matchId) {
        this.matchId = matchId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void init() {
        if (this.initialized) {
            return;
        }

        match = matchService.get(this.matchId);
        if(null == match) {
            RuntimeException e = new RuntimeException("赛事数据未保存在数据库中");
            log.error(e.getMessage(),e);
            throw e;
        }

        this.cfgs = cfgLoader.gets(this.matchId);
        this.initPatterns(cfgs);
        this.initialized = true;
    }

    private void initPatterns(Collection<BasePatternConfig> cfgs) {
        List<AbstractMatchPattern> patterns = new ArrayList<>();
        for (BasePatternConfig cfg : cfgs) {
            AbstractMatchPattern pattern = MatchHelper.newFactory(cfg);
            //pattern.setDataProvider(this.provider);
            pattern.init();
            patterns.add(pattern);
        }
        //设置上下引用关系
//        for (int i = 0; i < patterns.size(); i++) {
//            AbstractMatchPattern cMr = patterns.get(i);
//            AbstractMatchPattern nMr = null;
//            if (i + 1 < patterns.size()) {
//                nMr = patterns.get(i + 1);
//            }
//            if (i == 0) {
//                cMr.setNextPattern(nMr);
//            } else {
//                AbstractMatchPattern pMr = patterns.get(i - 1);
//                cMr.setNextPattern(nMr);
//                cMr.setPreviousPattern(pMr);
//            }
//        }

        if (patterns.size() > 0) {
            this.startPattern = patterns.get(0);
        }
    }

    @Override
    public synchronized void addPattern(BasePatternConfig cfg) {
        for (BasePatternConfig mpc : this.cfgs) {
            if (mpc.getPatternId() == cfg.getPatternId()) {
                return;
            }
        }
        this.cfgs.add(cfg);
    }

    @Override
    public void reset() {
        AbstractMatchPattern fMp = this.startPattern;
        while (fMp != null) {
            fMp.reset();
            fMp = null;
            //fMp = fMp.getNextPattern();
        }
        this.matchService.saveState(this.matchId,MatchStates.UnInitialize);
    }

    public synchronized void saveCfgs() {
        int pid = 0;
        for (BasePatternConfig cfg : this.cfgs) {
            cfg.setPid(pid);
            //this.provider.saveMPCfg(cfg);
            pid = cfg.getPatternId();
        }
    }

//    public synchronized MResult removePattern(String patternId) {
//        PatternStates state = this.provider.getPatternState(patternId);
//        if (state != PatternStates.UnBuildSchedule) {
//            return new MResult("1102", "赛程已构建,不能删除");
//        }
//        BasePatternConfig cfg = null;
//        for (BasePatternConfig _cfg : this.cfgs) {
//            if (_cfg.getPatternId().equals(patternId)) {
//                cfg = _cfg;
//                break;
//            }
//        }
//        if (cfg != null) {
//            this.cfgs.remove(cfg);
//            this.initPatterns(this.cfgs);
//            return new MResult(MResult.SUCCESS_CODE, "删除成功");
//        }
//        return new MResult("1103", "配置不存在");
//    }

//    public List<BasePatternConfig> getCfgs() {
//        List<BasePatternConfig> cfgs = new ArrayList<>();
//        BasePatternConfig cfg = this.provider.getMPCfg(this.matchId, 0);
//        if (cfg == null)
//            return cfgs;
//        cfgs.add(cfg);
//        for (; ; ) {
//            cfg = this.provider.getMPCfg(this.matchId, cfg.getPatternId());
//            if (cfg != null)
//                cfgs.add(cfg);
//            else
//                break;
//        }
//        return cfgs;
//    }
//
//    public void setDataProvider(IPatternDataProvider provider) {
//        this.provider = provider;
//    }

    @Override
    public MResult buildSchedule(int maxPlayers) {
        this.initPatterns(this.cfgs);
        AbstractMatchPattern fMp = this.startPattern;
        if (fMp != null) {
            fMp.init();
            return fMp.buildSchedule(maxPlayers);
        }
        return new MResult("1100", "没有可供构建的赛程");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void save() {
        AbstractMatchPattern fMp = this.startPattern;
        while (fMp != null) {
            boolean ok = fMp.save();
            if(!ok ) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return;
            }
            fMp = null;
            //fMp = fMp.getNextPattern();
        }
        this.matchService.saveState(this.matchId,MatchStates.Initialized);
    }
//
//    public AbstractMatchPattern getPattern(String patternId) {
//        AbstractMatchPattern fMp = this.startPattern;
//        while (fMp != null) {
//            if (fMp.getPatternId().equals(patternId))
//                return fMp;
//            fMp = fMp.getNextPattern();
//        }
//        return null;
//    }
//
//    public Collection<AbstractMatchPattern> getPatterns() {
//        List<AbstractMatchPattern> mps = new ArrayList<>();
//        AbstractMatchPattern mp = this.startPattern;
//        for (; ; ) {
//            if (mp != null) {
//                mps.add(mp);
//                mp = mp.getNextPattern();
//            } else {
//                break;
//            }
//        }
//        return mps;
//    }
}
