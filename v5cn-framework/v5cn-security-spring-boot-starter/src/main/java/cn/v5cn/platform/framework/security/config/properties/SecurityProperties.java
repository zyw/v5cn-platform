package cn.v5cn.platform.framework.security.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Security 配置属性
 */
@Data
@ConfigurationProperties(prefix = "v5cn.security")
public class SecurityProperties {
    /**
     * 排除路径
     */
    private String[] excludes;
}
