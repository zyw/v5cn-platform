package cn.v5cn.platform.framework.redis.manager;

import cn.v5cn.platform.framework.core.utils.SpringUtils;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * Cache装饰器模式（用于扩展Caffeine一级缓存）
 */
public class CaffeineCacheDecorator implements Cache {

    private static final com.github.benmanes.caffeine.cache.Cache<Object, Object>
            CAFFEINE = SpringUtils.getBean("caffeine");

    private final Cache cache;

    public CaffeineCacheDecorator(Cache cache) {
        this.cache = cache;
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public Object getNativeCache() {
        return cache.getNativeCache();
    }

    public String getUniqueKey(Object key) {
        return cache.getName() + ":" + key;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object o = CAFFEINE.get(getUniqueKey(key), k -> cache.get(key));
        return (ValueWrapper)o;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object o = CAFFEINE.get(getUniqueKey(key), k -> cache.get(key, type));
        return (T) o;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object o = CAFFEINE.get(getUniqueKey(key), k -> cache.get(key, valueLoader));
        return (T) o;
    }

    @Override
    public void put(Object key, Object value) {
        CAFFEINE.invalidate(getUniqueKey(key));
        cache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        CAFFEINE.invalidate(getUniqueKey(key));
        return cache.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        evictIfPresent(key);
    }

    public boolean evictIfPresent(Object key) {
        boolean b = cache.evictIfPresent(key);
        if (b) {
            CAFFEINE.invalidate(getUniqueKey(key));
        }
        return b;
    }

    @Override
    public void clear() {
        cache.clear();
    }

    public boolean invalidate() {
        return cache.invalidate();
    }
}
