package com.cj.engine.core;

import com.cj.engine.storage.IPlayerAssignStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 随机分配选手
 */
@Service
public class RandomPlayerAssignStrategy implements IPlayerAssignStrategy {
    @Override
    public MResult assign(AbstractMatchPattern pattern, Collection<EnrollPlayer> players) {
        ArrayList<EnrollPlayer> rndPlayers = new ArrayList<>();
        ArrayList<EnrollPlayer> rndSeeders = new ArrayList<>();

        if(players != null) {
            for (EnrollPlayer p : players) {
                if (p.getType() == PlayerTypes.Enroller) {
                    rndPlayers.add(p);
                }
                if (p.getType() == PlayerTypes.Seeder) {
                    rndSeeders.add(p);
                }
            }
        }
        Collections.shuffle(rndPlayers);
        Collections.shuffle(rndSeeders);

        int idx = 0;
        int i = 0;
        MatchRound mr = pattern.getMatchRound(1);
        List<VsGroup> groups = pattern.getVsGroups(mr.getId());
        for (EnrollPlayer player : rndSeeders) {
            VsGroup group = groups.get(i);
            List<VsNode> nodes = pattern.getVsNodes(group.getId());
            VsNode node = nodes.get(idx);
            node.setPlayerId(player.getPlayerId());
            player.setNodeId(node.getId());
            player.modify();
            node.modify();
            i++;
            if (i == groups.size()) {
                idx++;
                i = 0;
            }
        }

        for (EnrollPlayer player : rndPlayers) {
            VsGroup group = groups.get(i);
            List<VsNode> nodes = pattern.getVsNodes(group.getId());
            VsNode node = nodes.get(idx);
            node.setPlayerId(player.getPlayerId());
            player.setNodeId(node.getId());
            player.modify();
            node.modify();
            i++;
            if (i == groups.size()) {
                idx++;
                i = 0;
            }
        }

        return new MResult(MResult.SUCCESS_CODE, "分配成功");
    }
}
