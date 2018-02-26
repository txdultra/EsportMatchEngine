package com.cj.engine.storage.impl;

import com.cj.engine.core.IDataService;
import com.cj.engine.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService implements IDataService {

    @Autowired
    private IMatchRoundStorage matchRoundStorage;
    @Autowired
    private IVsGroupStorage vsGroupStorage;
    @Autowired
    private IVsNodeStorage vsNodeStorage;
    @Autowired
    private IMatchVsStorage matchVsStorage;
    @Autowired
    private IEnrollPlayerStorage enrollPlayerStorage;
    @Autowired
    private IPlayerAssignStrategy assignStrategy;
    @Autowired
    private IPatternStorage patternStorage;
    @Autowired
    private IGroupStageStorage stageStorage;

    @Override
    public IMatchRoundStorage getMatchRoundStorage() {
        return this.matchRoundStorage;
    }

    @Override
    public IVsGroupStorage getVsGroupStorage() {
        return this.vsGroupStorage;
    }

    @Override
    public IVsNodeStorage getVsNodeStorage() {
        return this.vsNodeStorage;
    }

    @Override
    public IMatchVsStorage getMatchVsStorage() {
        return this.matchVsStorage;
    }

    @Override
    public IEnrollPlayerStorage getEnrollPlayerStorage() {
        return this.enrollPlayerStorage;
    }

    @Override
    public IPlayerAssignStrategy getAssignStrategy() {
        return this.assignStrategy;
    }

    @Override
    public IPatternStorage getPatternStorage() {
        return this.patternStorage;
    }

    @Override
    public IGroupStageStorage getGroupStageStorage() {
        return this.stageStorage;
    }
}
