package cn.v5cn.platform.framework.redis.handler;

import jodd.util.StringUtil;
import org.redisson.api.NameMapper;

/**
 * Redis缓存key前缀处理器
 */
public class KeyPrefixHandler implements NameMapper {
    private final String keyPrefix;

    public KeyPrefixHandler(String keyPrefix) {
        //前缀为空 则返回空前缀
        this.keyPrefix = StringUtil.isBlank(keyPrefix) ? "" : keyPrefix + ":";
    }

    /**
     * 增加前缀
     */
    @Override
    public String map(String name) {
        if(StringUtil.isBlank(name)) {
            return null;
        }
        if (StringUtil.isNotBlank(keyPrefix) && !name.startsWith(keyPrefix)) {
            return keyPrefix + name;
        }
        return name;
    }

    /**
     * 去除前缀
     */
    @Override
    public String unmap(String name) {
        if(StringUtil.isBlank(name)) {
            return null;
        }
        if(StringUtil.isNotBlank(keyPrefix) && name.startsWith(keyPrefix)) {
            return name.substring(keyPrefix.length());
        }
        return name;
    }
}
