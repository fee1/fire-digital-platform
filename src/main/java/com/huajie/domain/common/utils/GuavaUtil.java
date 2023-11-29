package com.huajie.domain.common.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuxiaofeng
 * @date 2023/11/29
 */
public class GuavaUtil {

    private static ConcurrentHashMap<Integer, Cache<String, Object>> caches = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, Integer> key2Cache = new ConcurrentHashMap<>();

    private static Cache<String, Object> getCache(Integer timeout){
        if (caches.get(timeout) != null){
            return caches.get(timeout);
        }
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (timeout > 0) {
            //当缓存项在指定的时间段内没有更新就会被回收
            builder.expireAfterWrite(timeout, TimeUnit.SECONDS);
        } else {
            //当缓存项上一次更新操作之后的多久会被刷新
            builder.expireAfterAccess(timeout, TimeUnit.SECONDS);
        }
        Cache<String, Object> cache = builder.build();
        caches.put(timeout, cache);
        return cache;
    }

    public static void set(String key, Object value, Integer timeout){
        remove(key);
        Cache<String, Object> cache = getCache(timeout);
        cache.put(key, value);
        key2Cache.put(key, timeout);
    }

    public static <T> T get(String key){
        Integer targetCacheIndex = key2Cache.get(key);
        Object value = null;
        if (targetCacheIndex != null) {
            Cache<String, Object> stringObjectCache = caches.get(targetCacheIndex);
            value = stringObjectCache.getIfPresent(key);
            if (value == null){
                key2Cache.remove(key);
            }
        }
        return (T) value;
    }

    public static void remove(String key){
        Integer targetCacheIndex = key2Cache.get(key);
        if (targetCacheIndex != null) {
            Cache<String, Object> stringObjectCache = caches.get(targetCacheIndex);
            stringObjectCache.invalidate(key);
            key2Cache.remove(key);
        }
    }

}
