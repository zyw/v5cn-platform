package cn.v5cn.platform.framework.protection.idempotent.core.aop;

import cn.v5cn.platform.framework.core.constant.GlobalConstants;
import cn.v5cn.platform.framework.core.exception.ServiceException;
import cn.v5cn.platform.framework.core.utils.StreamUtils;
import cn.v5cn.platform.framework.protection.idempotent.core.annotation.Idempotent;
import cn.v5cn.platform.framework.protection.idempotent.core.keyresolver.IdempotentKeyResolver;
import cn.v5cn.platform.framework.protection.idempotent.core.utils.IdempotentRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * 拦截声明了{@link Idempotent } 注解的方法，实现幂等操作
 */
@Slf4j
@Aspect
public class IdempotentAspect {
    /**
     * IdempotentKeyResolver 集合
     */
    private final Map<Class<? extends IdempotentKeyResolver>, IdempotentKeyResolver> keyResolvers;

    public IdempotentAspect(List<IdempotentKeyResolver> keyResolvers) {
        this.keyResolvers = StreamUtils.toIdentityMap(keyResolvers, IdempotentKeyResolver::getClass);
    }

    @Around("@annotation(idempotent)")
    public Object aroundPointCut(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        // 获得 IdempotentKeyResolver
        IdempotentKeyResolver keyResolver = keyResolvers.get(idempotent.keyResolver());
        Assert.notNull(keyResolver, "找不到对应的 IdempotentKeyResolver");
        // 解析 Key
        String key = keyResolver.resolver(joinPoint, idempotent);

        // 1. 锁定 Key
        boolean success = IdempotentRedisUtils.setIfAbsent(key, idempotent.timeout(), idempotent.timeUnit());
        if (!success) {
            log.info("[aroundPointCut][方法({}) 参数({}) 存在重复请求]", joinPoint.getSignature().toString(), joinPoint.getArgs());
            throw new ServiceException(idempotent.message(), GlobalConstants.REPEATED_REQUESTS);
        }
        // 2. 执行逻辑
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            // 3. 异常时，删除 Key
            // 参考美团 GTIS 思路：https://tech.meituan.com/2016/09/29/distributed-system-mutually-exclusive-idempotence-cerberus-gtis.html
            if (idempotent.deleteKeyWhenException()) {
                IdempotentRedisUtils.delete(key);
            }
            throw e;
        }
    }
}
