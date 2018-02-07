package com.cj.engine.core;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by tang on 2016/4/13.
 */
@Getter
@Setter
public class GroupVsNode extends VsNode{
    public GroupVsNode(String id) {
        super(id);
    }

    private int wins;
    private int loses;
    private int pings;

    public void addWins(int ws) {this.wins += ws;}

    public void addLoses(int ls) {this.loses += ls;}

    public void addPings(int pings) {this.pings += pings;}
}
