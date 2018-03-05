package com.cj.engine.core;

import com.cj.engine.core.cfg.BasePatternConfig;

public interface IMatchEngine {

    /**
     * 初始化赛事引擎
     */
    void init();

    /**
     * 生成赛程
     * @param maxPlayers
     * @return
     */
    MResult buildSchedule(int maxPlayers);

    /**
     * 保存赛程状态
     */
    void save();

    /**
     * 添加赛事规则
     * @param cfg
     */
    void addPattern(BasePatternConfig cfg);

    /**
     * 重置赛事
     */
    void reset();

    /**
     * 获取赛事模型
     * @param patternId
     * @return
     */
    AbstractMatchPattern getPattern(int patternId);
}
