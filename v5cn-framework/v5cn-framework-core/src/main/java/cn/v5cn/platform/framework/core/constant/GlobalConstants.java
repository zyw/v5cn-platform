package cn.v5cn.platform.framework.core.constant;

/**
 * 全局的key常量 (业务无关的key)
 */
public interface GlobalConstants {

    /**
     * 全局 redis key (业务无关的key)
     */
    String GLOBAL_REDIS_KEY = "global:";

    /**
     * 重复请求，请稍后重试
     */
    int REPEATED_REQUESTS = 900;

    /**
     * 请求失败，请稍后重试
     */
    int LOCKED = 423;

    /**
     * 请求过于频繁，请稍后重试
     */
    int TOO_MANY_REQUESTS = 429;
}
