package com.cj.engine.core;

import com.cj.engine.storage.*;

public interface IDataService {
     IMatchRoundStorage getMatchRoundStorage();

     IVsGroupStorage getVsGroupStorage();

     IVsNodeStorage getVsNodeStorage();

     IMatchVsStorage getMatchVsStorage();

     IEnrollPlayerStorage getEnrollPlayerStorage();

     IPlayerAssignStrategy getAssignStrategy();

     IPatternStorage getPatternStorage();

     IGroupStageStorage getGroupStageStorage();

}
