package cn.v5cn.platform.framework.satoken.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import cn.v5cn.platform.framework.core.factory.YmlPropertySourceFactory;
import cn.v5cn.platform.framework.satoken.core.dao.PlusSaTokenDao;
import cn.v5cn.platform.framework.satoken.core.service.SaPermissionImpl;
import cn.v5cn.platform.framework.satoken.handler.SaTokenExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * sa-token 自动配置类
 */
@AutoConfiguration
@PropertySource(value = "classpath:common-satoken.yml", factory = YmlPropertySourceFactory.class)
public class SaTokenAutoConfiguration {

    @Bean
    public StpLogic getStpLogicJwt() {
        // Sa-Token 整合 jwt（简单模式）
        return new StpLogicJwtForSimple();
    }

    /**
     * 权限接口实现（使用bean注入方便用户替换）
     */
    @Bean
    public StpInterface stpInterface() {
        return new SaPermissionImpl();
    }

    /**
     * 自定义dao层存储
     */
    @Bean
    public SaTokenDao saTokenDao() {
        return new PlusSaTokenDao();
    }

    /**
     * 异常处理器
     */
    @Bean
    public SaTokenExceptionHandler saTokenExceptionHandler() {
        return new SaTokenExceptionHandler();
    }
}
