package cn.v5cn.platform.framework.web.config;

import cn.v5cn.platform.framework.core.utils.StringUtils;
import cn.v5cn.platform.framework.web.config.properties.XssProperties;
import cn.v5cn.platform.framework.web.filter.RepeatableFilter;
import cn.v5cn.platform.framework.web.filter.XssFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(XssProperties.class)
public class FilterAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "xss.enabled", havingValue = "true")
    public FilterRegistrationBean xssFilterRegistration(XssProperties xssProperties) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns(StringUtils.split(xssProperties.getUrlPatterns(), StringUtils.SEPARATOR));
        registration.setName("xssFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);

        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("excludes", xssProperties.getExcludes());
        registration.setInitParameters(initParameters);
        return registration;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RepeatableFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableFilter");
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }
}
