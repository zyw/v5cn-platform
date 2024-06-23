package cn.v5cn.platform.framework.redis.utils;

import cn.v5cn.platform.framework.core.utils.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.*;

import java.time.Duration;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * redis 工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisUtils {

    private static final RedissonClient CLIENT = SpringUtils.getBean(RedissonClient.class);

    /**
     * 获取 redis 客户端
     * @return RedissonClient
     */
    public static RedissonClient getClient() {
        return CLIENT;
    }

    /**
     * 缓存基本对象，保留当前对象 TTL 有效期
     * @param key 缓存的键
     * @param value 缓存的值
     * @param isSaveTtl 是否保留TTL有效期(例如: set之前ttl剩余90 set之后还是为90)
     * @since Redis 6.X 以上使用 setAndKeepTTL 兼容 5.X 方案
     */
    public static <T> void setCacheObject(final String key, final T value, final boolean isSaveTtl) {
        RBucket<Object> rBucket = CLIENT.getBucket(key);
        if (isSaveTtl) {
            try {
                rBucket.setAndKeepTTL(value);
            } catch (Exception e) {
                long timeToLive = rBucket.remainTimeToLive();
                if (timeToLive == -1) {
                    setCacheObject(key, value);
                } else {
                    setCacheObject(key, value, Duration.ofMillis(timeToLive));
                }
            }
        } else {
            rBucket.set(value);
        }
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public static <T> void setCacheObject(final String key, final T value) {
        setCacheObject(key, value, false);
    }
    /**
     * 缓存基本对象，Integer、String、实体类等
     * @param key  缓存的键
     * @param value 缓存的值
     * @param duration 过期时间
     */
    public static <T> void setCacheObject(final String key, final T value, final Duration duration) {
        RBatch rBatch = CLIENT.createBatch();
        RBucketAsync<T> bucket = rBatch.getBucket(key);
        bucket.setAsync(value);
        bucket.expireAsync(duration);
        rBatch.execute();
    }

    /**
     * 如果不存在则设置，并返回true 如果存在则返回false
     * @param key 缓存的键
     * @param value 缓存的值
     * @param duration 超时时间
     * @return set成功或失败
     */
    public static <T> boolean setObjectIfAbsent(final String key, final T value, final Duration duration) {
        RBucket<Object> rBucket = CLIENT.getBucket(key);
        return rBucket.setIfAbsent(value, duration);
    }

    /**
     * 设置有效时间
     * @param key Redis Key
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout) {
        return expire(key, Duration.ofSeconds(timeout));
    }

    /**
     * 设置有效时间
     * @param key Redis Key
     * @param duration 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final Duration duration) {
        RBucket<Object> rBucket = CLIENT.getBucket(key);
        return rBucket.expire(duration);
    }

    /**
     * 获得缓存的基本对象
     * @param key 缓存的键值
     * @return 返回缓存对象
     */
    public static <T> T getCacheObject(final String key) {
        RBucket<T> rBucket = CLIENT.getBucket(key);
        return rBucket.get();
    }

    /**
     * 获取key剩余存活时间
     * @param key 缓存的键值
     * @return 返回存活时间，时间单位为毫秒，如果密钥不存在，则为-2。
     *         -1表示密钥存在，但没有相关的过期。
     */
    public static <T> long getTimeToLive(final String key) {
        RBucket<T> rBucket = CLIENT.getBucket(key);
        return rBucket.remainTimeToLive();
    }

    /**
     * 删除单个对象
     * @param key 缓存的键值
     * @return true/false
     */
    public static boolean deleteObject(final String key) {
        return CLIENT.getBucket(key).delete();
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     */
    public static void deleteObject(final Collection collection) {
        RBatch rBatch = CLIENT.createBatch();
        collection.forEach(t -> {
            rBatch.getBucket(t.toString()).deleteAsync();
        });
        rBatch.execute();
    }

    /**
     * 获得缓存的基本对象列表
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public static Collection<String> keys(final String pattern) {
        Stream<String> stream = CLIENT.getKeys().getKeysStreamByPattern(pattern);
        return stream.collect(Collectors.toList());
    }

    /**
     * 检查 redis 中是否存在指定 key
     * @param key 检查的key
     * @return true/false
     */
    public static Boolean hasKey(String key) {
        RKeys rKeys = CLIENT.getKeys();
        return rKeys.countExists(key) > 0;
    }

}
