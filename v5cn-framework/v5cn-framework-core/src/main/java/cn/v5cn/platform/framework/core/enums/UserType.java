package cn.v5cn.platform.framework.core.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 * 针对多套用户体系
 */

@Getter
@AllArgsConstructor
public enum UserType {

    /**
     * PC端
     */
    SYS_USER("sys_user"),
    /**
     * APP端
     */
    APP_USER("app_user");

    private final String userType;

    public static UserType getUserType(String userType) {
        for (UserType value : values()) {
            if(StrUtil.contains(userType,value.getUserType())) {
                return value;
            }
        }
        throw new RuntimeException("'UserType' not found By " + userType);
    }
}
