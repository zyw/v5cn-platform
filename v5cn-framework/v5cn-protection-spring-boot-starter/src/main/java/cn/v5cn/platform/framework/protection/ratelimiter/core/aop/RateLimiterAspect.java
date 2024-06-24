package cn.v5cn.platform.framework.protection.ratelimiter.core.aop;

import cn.v5cn.platform.framework.core.constant.GlobalConstants;
import cn.v5cn.platform.framework.core.exception.ServiceException;
import cn.v5cn.platform.framework.core.utils.StreamUtils;
import cn.v5cn.platform.framework.protection.ratelimiter.core.annotation.RateLimiter;
import cn.v5cn.platform.framework.protection.ratelimiter.core.keyresolver.RateLimiterKeyResolver;
import cn.v5cn.platform.framework.protection.ratelimiter.core.utils.RateLimiterRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * 拦截声明了 {@link RateLimiter} 注解的方法，实现限流操作
 *
 */
@Aspect
@Slf4j
public class RateLimiterAspect {

    /**
     * RateLimiterKeyResolver 集合
     */
    private final Map<Class<? extends RateLimiterKeyResolver>, RateLimiterKeyResolver> keyResolvers;

    public RateLimiterAspect(List<RateLimiterKeyResolver> keyResolvers) {
        this.keyResolvers = StreamUtils.toIdentityMap(keyResolvers, RateLimiterKeyResolver::getClass);
    }

    @Before("@annotation(rateLimiter)")
    public void beforePointCut(JoinPoint joinPoint, RateLimiter rateLimiter) {
        // 获得 IdempotentKeyResolver 对象
        RateLimiterKeyResolver keyResolver = keyResolvers.get(rateLimiter.keyResolver());
        Assert.notNull(keyResolver, "找不到对应的 RateLimiterKeyResolver");
        // 解析 Key
        String key = keyResolver.resolver(joinPoint, rateLimiter);

        // 获取 1 次限流
        boolean success = RateLimiterRedisUtils.tryAcquire(key,
                rateLimiter.count(), rateLimiter.time(), rateLimiter.timeUnit());
        if (!success) {
            log.info("[beforePointCut][方法({}) 参数({}) 请求过于频繁]", joinPoint.getSignature().toString(), joinPoint.getArgs());
            throw new ServiceException(rateLimiter.message(), GlobalConstants.TOO_MANY_REQUESTS);
        }
    }

}

