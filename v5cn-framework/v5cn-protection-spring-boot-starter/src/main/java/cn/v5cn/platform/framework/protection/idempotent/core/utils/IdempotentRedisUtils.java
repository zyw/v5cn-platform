package cn.v5cn.platform.framework.protection.idempotent.core.utils;

import cn.hutool.core.date.TemporalUtil;
import cn.v5cn.platform.framework.redis.utils.RedisUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class IdempotentRedisUtils {
    /**
     * 幂等操作
     *
     * KEY 格式：idempotent:%s // 参数为 uuid
     * VALUE 格式：String
     * 过期时间：不固定
     */
    private static final String IDEMPOTENT = "idempotent:%s";

    public static Boolean setIfAbsent(String key, long timeout, TimeUnit timeUnit) {
        String redisKey = formatKey(key);
        return RedisUtils.setObjectIfAbsent(redisKey, "", Duration.of(timeout, TemporalUtil.toChronoUnit(timeUnit)));
    }

    public static void delete(String key) {
        String redisKey = formatKey(key);
        RedisUtils.deleteObject(redisKey);
    }

    private static String formatKey(String key) {
        return String.format(IDEMPOTENT, key);
    }
}
