package cn.v5cn.platform.framework.web.config;

import cn.v5cn.platform.framework.web.core.I18nLocaleResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 国际化配置
 */
@AutoConfiguration(before = WebMvcAutoConfiguration.class)
public class I18nAutoConfiguration {

    @Bean
    public LocaleResolver i18nInterceptor() {
        return new I18nLocaleResolver();
    }
}
