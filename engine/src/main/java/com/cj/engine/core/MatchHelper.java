package com.cj.engine.core;


import com.cj.engine.cfg.SpringAppContext;
import com.cj.engine.core.cfg.BasePatternConfig;

import java.util.UUID;

/**
 * Created by tang on 2016/3/22.
 */
public class MatchHelper {
    public synchronized static AbstractMatchPattern newFactory(BasePatternConfig cfg) {
        IDataService dataService = SpringAppContext.getBean(IDataService.class);
        switch (cfg.getType()) {
            case Single:
                return new SingleMatchPattern(cfg, dataService);
            case Double:
                return new DoubleMatchPattern(cfg, dataService);
            case Group:
                return new GroupMatchPattern(cfg, dataService);
            default:
                throw new IllegalArgumentException("参数错误");
        }
    }

    public synchronized static String getItemId(PatternTypes pt, PatternItemTypes it) {
        String uuid = getRandomString().substring(0, 10);
        String itName = "";
        switch (it) {
            case Group:
                itName = "g";
                break;
            case Round:
                itName = "r";
                break;
            case Node:
                itName = "n";
                break;
            default:
                itName = "x";
                break;
        }
        switch (pt) {
            case Single:
                return "s" + itName + "-" + uuid;
            case Double:
                return "d" + itName + "-" + uuid;
            case Group:
                return "g" + itName + "-" + uuid;
            case GSL:
                return "l" + itName + "-" + uuid;
            default:
                return "";
        }
    }

    public synchronized static String NewPatternId(PatternTypes pt) {
        String uuid = getRandomString().substring(0, 10);
        switch (pt) {
            case Single:
                return "s-" + uuid;
            case Double:
                return "d-" + uuid;
            case Group:
                return "g-" + uuid;
            case GSL:
                return "l-" + uuid;
            default:
                return "";
        }
    }

    public synchronized static String getRandomString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
