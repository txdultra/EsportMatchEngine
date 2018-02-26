package com.cj.engine.core;

import com.cj.engine.core.cfg.BasePatternConfig;
import com.cj.engine.core.cfg.DoublePatternConfig;
import com.cj.engine.core.cfg.GroupPatternConfig;
import com.cj.engine.core.cfg.SinglePatternConfig;
import com.cj.engine.model.MatchPatternInfo;
import com.cj.engine.storage.IPatternStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatternConfigLoaderImpl implements PatternConfigLoader {

    @Autowired
    private IPatternStorage patternService;

    @Override
    public List<BasePatternConfig> gets(int matchId) {
        List<MatchPatternInfo> configs = patternService.gets(matchId);
        List<BasePatternConfig> cfgs = new ArrayList<>();
        for (MatchPatternInfo mpi : configs) {
            cfgs.add(convert(mpi));
        }
        return cfgs;
    }

    private BasePatternConfig convert(MatchPatternInfo mpi) {
        BasePatternConfig cfg = null;
        switch (mpi.getType()) {
            case Single:
                cfg = new SinglePatternConfig();
                break;
            case Double:
                cfg = new DoublePatternConfig();
                break;
            case Group:
                cfg = new GroupPatternConfig();
                break;
            default:
                break;
        }
        cfg.setPatternId(mpi.getId());
        cfg.setGroupPlayerNumber(mpi.getGroupPlayers());
        cfg.setIndex(mpi.getIndex());
        cfg.setMatchId(mpi.getMatchId());
        cfg.setPid(mpi.getPid());
        cfg.setPromotions(mpi.getPromotions());
        cfg.setType(mpi.getType());
        cfg.setVerdictType(VerdictTypes.VERDICT);
        cfg.putAll(mpi.getMap());
        return cfg;
    }

    @Override
    public BasePatternConfig get(int patternId) {
        MatchPatternInfo mpi = patternService.get(patternId);
        return convert(mpi);
    }
}
