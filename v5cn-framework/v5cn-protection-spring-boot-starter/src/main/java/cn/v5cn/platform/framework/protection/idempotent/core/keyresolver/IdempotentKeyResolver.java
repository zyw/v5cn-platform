package cn.v5cn.platform.framework.protection.idempotent.core.keyresolver;

import cn.v5cn.platform.framework.protection.idempotent.core.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;

/**
 * 幂等 Key 解析器接口
 */
public interface IdempotentKeyResolver {

    /**
     * 解析一个 Key
     * @param joinPoint AOP 切面
     * @param idempotent 幂等注解
     * @return 幂等 Key
     */
    String resolver(JoinPoint joinPoint, Idempotent idempotent);
}
