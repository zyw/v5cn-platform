package cn.v5cn.platform.framework.satoken.core.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.v5cn.platform.framework.core.domain.model.LoginUser;
import cn.v5cn.platform.framework.core.enums.UserType;
import cn.v5cn.platform.framework.satoken.utils.LoginHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * sa-token 权限管理实现类
 */
public class SaPermissionImpl implements StpInterface {

    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        UserType userType = UserType.getUserType(loginUser.getUserType());
        if (userType == UserType.SYS_USER) {
            return new ArrayList<>(loginUser.getMenuPermission());
        } else if (userType == UserType.APP_USER) {
            // TODO 其他端权限
        }
        return new ArrayList<>();
    }

    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object o, String s) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        UserType userType = UserType.getUserType(loginUser.getUserType());
        if (userType == UserType.SYS_USER) {
            return new ArrayList<>(loginUser.getRolePermission());
        } else if (userType == UserType.APP_USER) {
            // TODO 其他端角色
        }
        return new ArrayList<>();
    }
}
