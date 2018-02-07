package com.cj.engine.core;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/3/15.
 */
@Getter
@Setter
public class SeedPlayer extends EnrollPlayer{
    @Override
    public PlayerTypes getPlayerType() {
        return PlayerTypes.Seeder;
    }

    private Integer seedLvlId;
}
