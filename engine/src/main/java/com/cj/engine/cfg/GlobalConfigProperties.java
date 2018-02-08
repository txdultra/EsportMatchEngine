package com.cj.engine.cfg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tangxd
 * @Description: TODO
 * @date 2017/12/14
 */
@ConfigurationProperties("match.setting")
@Component
@Getter
@Setter
public class GlobalConfigProperties {
    private int actorProcessTimeout = 90;
}
