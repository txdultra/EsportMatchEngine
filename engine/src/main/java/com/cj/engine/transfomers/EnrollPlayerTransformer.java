package com.cj.engine.transfomers;

import com.cj.engine.core.EnrollPlayer;
import com.cj.engine.core.SeedPlayer;
import com.cj.engine.model.EnrollPlayerInfo;
import org.springframework.stereotype.Component;

@Component
public class EnrollPlayerTransformer implements DocTransformer<EnrollPlayerInfo, EnrollPlayer> {
    @Override
    public EnrollPlayer transform(EnrollPlayerInfo data) {
        EnrollPlayer player;
        switch (data.getType()) {
            case Seeder:
                player = new SeedPlayer();
                ((SeedPlayer)player).setSeedLvlId(data.getLevelId());
                break;
            default:
                player = new EnrollPlayer();
                break;
        }
        player.setPlayerId(data.getPlayerId());
        player.setMatchId(data.getMatchId());
        player.setFirstNodeId(data.getNodeId());
        player.setProperties(data.getProperties());
        return player;
    }
}
