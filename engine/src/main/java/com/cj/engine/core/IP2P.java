package com.cj.engine.core;

import com.cj.engine.core.VsNode;

/**
 * Created by tang on 2016/4/18.
 */
public interface IP2P {
    int promotionNextPatternPlayerCounts();
    void gotoNextPattern(int playerId);
    void receivePrevPattern(VsNode srcNode);
}
