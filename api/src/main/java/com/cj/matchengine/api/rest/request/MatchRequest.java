package com.cj.matchengine.api.rest.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>Create Time: 2018年03月06日</p>
 * <p>@author tangxd</p>
 **/
@Setter
@Getter
public class MatchRequest implements Serializable {
    private String title;
    private Map<String, Object> properties;
}
