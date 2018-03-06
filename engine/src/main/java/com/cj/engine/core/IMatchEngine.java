package com.cj.engine.core;

import com.cj.engine.core.cfg.BasePatternConfig;

import java.util.Collection;

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

    /**
     * 获取第一个模型
     * @return
     */
    AbstractMatchPattern getFirstPattern();

    /**
     * 获取所有赛事模型
     */
    Collection<AbstractMatchPattern> getPatterns();

    /**
     * 分配选手
     * @return
     */
    MResult assignPlayers();

    /**
     * 是否预览
     * @param isPreview
     */
    void setIsPreview(boolean isPreview);
}
