package com.cj.engine.core;

import com.cj.engine.service.*;

public interface IDataService {
     IMatchRoundService getMatchRoundService();

     IVsGroupService getVsGroupService();

     IVsNodeService getVsNodeService();

     IMatchVsService getMatchVsService();

     IEnrollPlayerService getEnrollPlayerService();

     IPlayerAssignStrategy getAssignStrategy();

     IPatternService getPatternService();

//     ICommonService getCommonService();
}
