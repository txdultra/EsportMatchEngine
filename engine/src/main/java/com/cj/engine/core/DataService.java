package com.cj.engine.core;

import com.cj.engine.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService implements IDataService {

    @Autowired
    private IMatchRoundService matchRoundService;
    @Autowired
    private IVsGroupService vsGroupService;
    @Autowired
    private IVsNodeService vsNodeService;
    @Autowired
    private IMatchVsService matchVsService;
    @Autowired
    private IEnrollPlayerService enrollPlayerService;
    @Autowired
    private IPlayerAssignStrategy assignStrategy;
    @Autowired
    private IPatternService patternService;

    @Override
    public IMatchRoundService getMatchRoundService() {
        return this.matchRoundService;
    }

    @Override
    public IVsGroupService getVsGroupService() {
        return this.vsGroupService;
    }

    @Override
    public IVsNodeService getVsNodeService() {
        return this.vsNodeService;
    }

    @Override
    public IMatchVsService getMatchVsService() {
        return this.matchVsService;
    }

    @Override
    public IEnrollPlayerService getEnrollPlayerService() {
        return this.enrollPlayerService;
    }

    @Override
    public IPlayerAssignStrategy getAssignStrategy() {
        return this.assignStrategy;
    }

    @Override
    public IPatternService getPatternService() {
        return this.patternService;
    }
}
