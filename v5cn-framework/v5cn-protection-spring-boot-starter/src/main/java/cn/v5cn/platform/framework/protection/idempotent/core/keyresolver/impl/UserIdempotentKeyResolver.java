package cn.v5cn.platform.framework.protection.idempotent.core.keyresolver.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.v5cn.platform.framework.core.domain.model.LoginUser;
import cn.v5cn.platform.framework.protection.idempotent.core.annotation.Idempotent;
import cn.v5cn.platform.framework.protection.idempotent.core.keyresolver.IdempotentKeyResolver;
import cn.v5cn.platform.framework.satoken.utils.LoginHelper;
import org.aspectj.lang.JoinPoint;

/**
 * 用户级别的幂等 Key 解析器，使用方法名 + 方法参数 + userId + userType，组装成一个 Key
 *
 * 为了避免 Key 过长，使用 MD5 进行“压缩”
 */
public class UserIdempotentKeyResolver implements IdempotentKeyResolver {


    @Override
    public String resolver(JoinPoint joinPoint, Idempotent idempotent) {
        String methodName = joinPoint.getSignature().toString();
        String argsStr = StrUtil.join(",", joinPoint.getArgs());
        LoginUser loginUser = LoginHelper.getLoginUser();
        Long userId = loginUser.getUserId();
        String userType = loginUser.getUserType();
        return SecureUtil.md5(methodName + argsStr + userId + userType);
    }
}
