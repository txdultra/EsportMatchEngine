package com.cj.engine.service;

import com.cj.engine.core.EnrollPlayer;
import com.cj.engine.core.MResult;
import com.cj.engine.core.AbstractMatchPattern;

import java.util.Collection;

/**
 * Created by tang on 2016/3/16.
 */
public interface IPlayerAssignStrategy {
    MResult assign(AbstractMatchPattern pattern, Collection<EnrollPlayer> players);
}
