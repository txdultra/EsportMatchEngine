package com.cj.engine.cfg.caching;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * @author tangxd
 * @Description: TODO
 * @date 2017/10/27
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JedisCache implements Cache {

    @Autowired
    private JedisPool jedisCluster;

    @Autowired
    private CacheObjectSerializer serializer;

    @Override
    public <T> T get(String key, Class<T> valueType) {
        String str;
        try (Jedis jedis = jedisCluster.getResource()) {
            str = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        return serializer.deserialize(str, valueType);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> valueType) {
        String str;
        List<T> list = new ArrayList<>();
        try (Jedis jedis = jedisCluster.getResource()){
            str = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        if (Strings.isNullOrEmpty(str)) {
            return Collections.emptyList();
        }
        ArrayList array = serializer.deserialize(str, ArrayList.class);
        try {
            for (Object anArray : array) {
                if (anArray instanceof Integer
                        || anArray instanceof String
                        || anArray instanceof Double
                        || anArray instanceof Float
                        || anArray instanceof Long
                        || anArray instanceof Boolean
                        || anArray instanceof Date) {
                    T obj = (T) anArray;
                    list.add(obj);
                } else {
                    T obj = JSONObject.toJavaObject((JSONObject) anArray, valueType);
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public <T> boolean set(String key, T obj, long seconds) {
        if (null == obj) {
            return false;
        }
        String str = serializer.serialize(obj);
        try (Jedis jedis = jedisCluster.getResource()){
            return "OK".equals(jedis.setex(key, Long.valueOf(seconds).intValue(), str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean del(String key) {
        try (Jedis jedis = jedisCluster.getResource()){
            return jedis.del(key) == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public long dels(String[] keys) {
        long c = 0;
        try (Jedis jedis = jedisCluster.getResource()){
            for (String key : keys) {
                c = c + jedis.del(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public <T> Collection<T> gets(Collection<String> keys, Class<T> valueType) {
        List<String> strs = new ArrayList<>();
        try (Jedis jedis = jedisCluster.getResource()){
            for (String key : keys) {
                strs.add(jedis.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<T> list = new ArrayList<>();
        for (String str : strs) {
            if (Strings.isNullOrEmpty(str)) {
                continue;
            }
            T obj = serializer.deserialize(str, valueType);
            list.add(obj);
        }
        return list;
    }
}
