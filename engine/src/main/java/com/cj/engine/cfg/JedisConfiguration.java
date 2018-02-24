package com.cj.engine.cfg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

@Configuration
@Slf4j
public class JedisConfiguration {

    @Autowired
    private JedisProperties jedisProperties;

//    @Bean
//    public JedisCluster jedisCluster() {
//        List<String> nodes = jedisProperties.getCluster().getNodes();
//        Set<HostAndPort> hps = new HashSet<>();
//        for (String node :nodes) {
//            String[] hostPort = node.split(":");
//            hps.add(new HostAndPort(hostPort[0].trim(),Integer.valueOf(hostPort[1].trim())));
//        }
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxIdle(jedisProperties.getPool().getMaxIdle());
//        poolConfig.setMinIdle(jedisProperties.getPool().getMinIdle());
//        poolConfig.setMaxWaitMillis(jedisProperties.getPool().getMaxWait());
//        return new JedisCluster(
//                hps,
//                jedisProperties.getTimeout(),
//                jedisProperties.getSoTimeout(),
//                jedisProperties.getMaxAttempts(),
//                jedisProperties.getPassword(),
//                poolConfig);
//    }


    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(jedisProperties.getPool().getMaxActive());
        jedisPoolConfig.setMaxIdle(jedisProperties.getPool().getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisProperties.getPool().getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(jedisProperties.getPool().getMaxWait());
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPool(JedisPoolConfig config) {
        return new JedisPool(
                config,
                jedisProperties.getHost(),
                jedisProperties.getPort(),
                jedisProperties.getTimeout(),
                jedisProperties.getPassword());
    }

//    /**
//     * redis连接的基础设置
//     * @Description:
//     * @return
//     */
//    @Bean
//    public JedisConnectionFactory redisConnectionFactory(JedisPoolConfig poolConfig) {
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//        factory.setHostName(jedisProperties.getHost());
//        factory.setPort(jedisProperties.getPort());
//        factory.setPassword(jedisProperties.getPassword());
//        //存储的库
//        factory.setDatabase(jedisProperties.getDatabase());
//        //设置连接超时时间
//        factory.setTimeout(jedisProperties.getTimeout());
//        factory.setUsePool(true);
//        factory.setPoolConfig(poolConfig);
//        return factory;
//    }

    /**
     * redis数据操作异常处理
     * 这里的处理：在日志中打印出错误信息，但是放行
     * 保证redis服务器出现连接等问题的时候不影响程序的正常运行，使得能够出问题时不用缓存
     * @return
     */
    @Bean
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.error("redis异常：key=[{}]", key, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.error("redis异常：key=[{}]", key, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.error("redis异常：key=[{}]", key, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("redis异常：", e);
            }
        };
        return cacheErrorHandler;
    }
}
