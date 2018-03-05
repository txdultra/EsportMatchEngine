package com.cj.engine.cfg.caching;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author tangxd
 * @Description: TODO
 * @date 2017/10/27
 */
public interface Cache {
    <T> T get(String key, Class<T> valueType);

    <T> List<T> getList(String key, Class<T> valueType);

    <T> boolean set(String key, T obj, long seconds);

    boolean del(String key);

    long dels(String[] keys);

    <T> Collection<T> gets(Collection<String> keys, Class<T> valueType);

    <T> T hget(String key, String fieldKey, Class<T> valueType);

    <T> boolean hset(String key,String fieldKey,T obj);

    <T> List<T> hgetall(String key,Class<T> valueType);

    boolean hdel(String key,String... fieldKeys);

    <T> void hmset(String key, Map<String, T> objs);
}
