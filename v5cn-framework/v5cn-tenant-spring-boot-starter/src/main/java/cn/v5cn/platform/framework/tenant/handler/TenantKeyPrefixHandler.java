package cn.v5cn.platform.framework.tenant.handler;

import cn.v5cn.platform.framework.core.constant.GlobalConstants;
import cn.v5cn.platform.framework.core.utils.StringUtils;
import cn.v5cn.platform.framework.redis.handler.KeyPrefixHandler;
import cn.v5cn.platform.framework.tenant.helper.TenantHelper;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 多租户redis缓存key前缀处理
 */
@Slf4j
public class TenantKeyPrefixHandler extends KeyPrefixHandler {

    public TenantKeyPrefixHandler(String keyPrefix) {
        super(keyPrefix);
    }

    /**
     * 增加前缀
     */
    @Override
    public String map(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        if (InterceptorIgnoreHelper.willIgnoreTenantLine("")) {
            return super.map(name);
        }
        if (StringUtils.contains(name, GlobalConstants.GLOBAL_REDIS_KEY)) {
            return super.map(name);
        }
        String tenantId = TenantHelper.getTenantId();
        if (StringUtils.isBlank(tenantId)) {
            log.error("无法获取有效的租户id -> Null");
        }
        if (StringUtils.startsWith(name, tenantId + "")) {
            // 如果存在则直接返回
            return super.map(name);
        }
        return super.map(tenantId + ":" + name);
    }

    @Override
    public String unmap(String name) {
        String unmap = super.unmap(name);
        if (StringUtils.isBlank(unmap)) {
            return null;
        }
        if (InterceptorIgnoreHelper.willIgnoreTenantLine("")) {
            return super.unmap(name);
        }
        if (StringUtils.contains(unmap, GlobalConstants.GLOBAL_REDIS_KEY)) {
            return super.unmap(name);
        }
        String tenantId = TenantHelper.getTenantId();
        if (StringUtils.isBlank(tenantId)) {
            log.error("无法获取有效的租户id -> Null");
        }
        if (StringUtils.startsWith(unmap, tenantId + "")) {
            // 如果存在则删除
            return unmap.substring((tenantId + ":").length());
        }
        return unmap;
    }
}
