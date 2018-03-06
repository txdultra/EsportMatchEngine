package com.cj.engine.core.cfg;

/**
 * Created by tang on 2016/4/18.
 */
public class SinglePatternConfig extends BasePatternConfig {

    private static final String HAS_CHAMPION_KEY = "has_champion";
    private static final String HAS_THRIDER_KEY = "Has_thrider";

    public boolean hasChampion() {
        if (containsKey(HAS_CHAMPION_KEY)) {
            return (Boolean) get(HAS_CHAMPION_KEY);
        }
        return false;
    }

    public void setHasChampion(boolean yesOrNo) {
        put(HAS_CHAMPION_KEY, yesOrNo);
    }

    public boolean hasThrider() {
        if (containsKey(HAS_THRIDER_KEY)) {
            return (Boolean) get(HAS_THRIDER_KEY);
        }
        return false;
    }

    public void setHasThrider(boolean yesOrNo) {
        put(HAS_THRIDER_KEY, yesOrNo);
    }

}
