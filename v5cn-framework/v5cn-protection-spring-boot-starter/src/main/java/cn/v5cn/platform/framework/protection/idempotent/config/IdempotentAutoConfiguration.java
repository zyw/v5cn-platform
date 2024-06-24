package cn.v5cn.platform.framework.protection.idempotent.config;

import cn.v5cn.platform.framework.protection.idempotent.core.aop.IdempotentAspect;
import cn.v5cn.platform.framework.protection.idempotent.core.keyresolver.IdempotentKeyResolver;
import cn.v5cn.platform.framework.protection.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import cn.v5cn.platform.framework.protection.idempotent.core.keyresolver.impl.GlobalIdempotentKeyResolver;
import cn.v5cn.platform.framework.protection.idempotent.core.keyresolver.impl.UserIdempotentKeyResolver;
import cn.v5cn.platform.framework.redis.config.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration(after = RedisAutoConfiguration.class)
public class IdempotentAutoConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers) {
        return new IdempotentAspect(keyResolvers);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public GlobalIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new GlobalIdempotentKeyResolver();
    }

    @Bean
    public UserIdempotentKeyResolver userIdempotentKeyResolver() {
        return new UserIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }
}
