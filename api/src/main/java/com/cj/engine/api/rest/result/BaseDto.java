package com.cj.engine.api.rest.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>Create Time: 2018年03月01日</p>
 * <p>@author tangxd</p>
 **/
@Getter
@Setter
public class BaseDto implements Serializable {
    private Map<String,Object> properties;
}
