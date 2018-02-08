package com.cj.engine.cfg.caching;

import java.util.Collection;
import java.util.List;

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
}
