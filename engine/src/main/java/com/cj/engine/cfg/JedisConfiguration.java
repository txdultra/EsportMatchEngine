package com.cj.engine.cfg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class JedisConfiguration {

    @Autowired
    private JedisProperties jedisProperties;

    @Bean
    public JedisCluster jedisCluster() {
        List<String> nodes = jedisProperties.getCluster().getNodes();
        Set<HostAndPort> hps = new HashSet<>();
        for (String node :nodes) {
            String[] hostPort = node.split(":");
            hps.add(new HostAndPort(hostPort[0].trim(),Integer.valueOf(hostPort[1].trim())));
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(jedisProperties.getPool().getMaxIdle());
        poolConfig.setMinIdle(jedisProperties.getPool().getMinIdle());
        poolConfig.setMaxWaitMillis(jedisProperties.getPool().getMaxWait());
        return new JedisCluster(
                hps,
                jedisProperties.getTimeout(),
                jedisProperties.getSoTimeout(),
                jedisProperties.getMaxAttempts(),
                jedisProperties.getPassword(),
                poolConfig);
    }
}
