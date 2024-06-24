package cn.v5cn.platform.framework.protection.lock4j.config;

import cn.v5cn.platform.framework.protection.lock4j.handler.Lock4jExceptionHandler;
import com.baomidou.lock.DefaultLockFailureStrategy;
import com.baomidou.lock.spring.boot.autoconfigure.LockAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = LockAutoConfiguration.class)
@ConditionalOnClass(name = "com.baomidou.lock.annotation.Lock4j")
public class Lock4jAutoConfiguration {

    @Bean
    public DefaultLockFailureStrategy lockFailureStrategy() {
        return new DefaultLockFailureStrategy();
    }

    /**
     * 异常处理器
     */
    @Bean
    public Lock4jExceptionHandler redisExceptionHandler() {
        return new Lock4jExceptionHandler();
    }
}
