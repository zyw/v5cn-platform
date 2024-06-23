package cn.v5cn.platform.framework.tenant.manager;

import cn.v5cn.platform.framework.core.constant.GlobalConstants;
import cn.v5cn.platform.framework.core.utils.StringUtils;
import cn.v5cn.platform.framework.redis.manager.PlusSpringCacheManager;
import cn.v5cn.platform.framework.tenant.helper.TenantHelper;
import org.springframework.cache.Cache;

/**
 * 重写 cacheName 处理方法 支持多租户
 */
public class TenantSpringCacheManager extends PlusSpringCacheManager {

    @Override
    public Cache getCache(String name) {
        if (StringUtils.contains(name, GlobalConstants.GLOBAL_REDIS_KEY)) {
            return super.getCache(name);
        }
        String tenantId = TenantHelper.getTenantId();
        if (StringUtils.startsWith(name, tenantId)) {
            // 如果存在则直接返回
            return super.getCache(name);
        }
        return super.getCache(tenantId + ":" + name);
    }
}
