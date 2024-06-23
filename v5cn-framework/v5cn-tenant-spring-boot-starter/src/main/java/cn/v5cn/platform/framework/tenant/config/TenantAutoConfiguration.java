package cn.v5cn.platform.framework.tenant.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.hutool.core.util.ObjectUtil;
import cn.v5cn.platform.framework.core.utils.reflect.ReflectUtils;
import cn.v5cn.platform.framework.mybatis.config.MybatisPlusAutoConfiguration;
import cn.v5cn.platform.framework.redis.config.RedisAutoConfiguration;
import cn.v5cn.platform.framework.redis.config.properties.RedissonProperties;
import cn.v5cn.platform.framework.tenant.config.properties.TenantProperties;
import cn.v5cn.platform.framework.tenant.core.TenantSaTokenDao;
import cn.v5cn.platform.framework.tenant.handler.PlusTenantLineHandler;
import cn.v5cn.platform.framework.tenant.handler.TenantKeyPrefixHandler;
import cn.v5cn.platform.framework.tenant.manager.TenantSpringCacheManager;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 租户配置类
 */
@EnableConfigurationProperties(TenantProperties.class)
@AutoConfiguration(after = RedisAutoConfiguration.class)
@ConditionalOnProperty(value = "v5cn.tenant.enabled", havingValue = "true")
public class TenantAutoConfiguration {

    @ConditionalOnBean(MybatisPlusAutoConfiguration.class)
    @AutoConfiguration(after = {MybatisPlusAutoConfiguration.class})
    static class MybatisPlusConfiguration {
        /**
         * 多租户插件
         */
        @Bean
        public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantProperties tenantProperties) {
            return new TenantLineInnerInterceptor(new PlusTenantLineHandler(tenantProperties));
        }
    }

    @Bean
    public RedissonAutoConfigurationCustomizer tenantRedissonCustomizer(RedissonProperties redissonProperties) {
        return config -> {
            TenantKeyPrefixHandler nameMapper = new TenantKeyPrefixHandler(redissonProperties.getKeyPrefix());
            SingleServerConfig singleServerConfig = ReflectUtils.invokeGetter(config, "singleServerConfig");
            if (ObjectUtil.isNotNull(singleServerConfig)) {
                // 使用单机模式
                // 设置多租户 redis key前缀
                singleServerConfig.setNameMapper(nameMapper);
                ReflectUtils.invokeSetter(config, "singleServerConfig", singleServerConfig);
            }
            ClusterServersConfig clusterServersConfig = ReflectUtils.invokeGetter(config, "clusterServersConfig");
            // 集群配置方式 参考下方注释
            if (ObjectUtil.isNotNull(clusterServersConfig)) {
                // 设置多租户 redis key前缀
                clusterServersConfig.setNameMapper(nameMapper);
                ReflectUtils.invokeSetter(config, "clusterServersConfig", clusterServersConfig);
            }
        };
    }

    /**
     * 租户缓存管理器
     */
    @Bean
    @Primary
    public CacheManager tenantCacheManager() {
        return new TenantSpringCacheManager();
    }

    /**
     * 多租户鉴权dao实现
     */
    @Bean
    @Primary
    public SaTokenDao tenantSaTokenDao() {
        return new TenantSaTokenDao();
    }
}
