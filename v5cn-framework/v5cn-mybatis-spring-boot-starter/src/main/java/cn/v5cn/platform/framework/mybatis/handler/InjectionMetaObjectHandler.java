package cn.v5cn.platform.framework.mybatis.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import cn.v5cn.platform.framework.core.domain.model.LoginUser;
import cn.v5cn.platform.framework.core.exception.ServiceException;
import cn.v5cn.platform.framework.mybatis.core.domain.BaseEntity;
import cn.v5cn.platform.framework.satoken.utils.LoginHelper;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * MP注入处理器
 */
@Slf4j
public class InjectionMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                LocalDateTime current = ObjectUtil.isNotNull(baseEntity.getCreatedTime()) ? baseEntity.getCreatedTime() : LocalDateTime.now();
                baseEntity.setCreatedTime(current);
                baseEntity.setUpdatedTime(current);
                if (ObjectUtil.isNull(baseEntity.getCreatedBy())) {
                    LoginUser loginUser = getLoginUser();
                    if (ObjectUtil.isNotNull(loginUser)) {
                        Long userId = loginUser.getUserId();
                        // 当前已登录 且 创建人为空 则填充
                        baseEntity.setCreatedBy(userId);
                        // 当前已登录 且 更新人为空 则填充
                        baseEntity.setUpdatedBy(userId);
                        baseEntity.setCreatedDept(ObjectUtil.isNotNull(baseEntity.getCreatedDept())
                                ? baseEntity.getCreatedDept() : loginUser.getDeptId());
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                LocalDateTime current = LocalDateTime.now();
                // 更新时间填充（不管为不为空）
                baseEntity.setUpdatedTime(current);
                // 当前已登录 更新人填充（不管为不为空）
                Long userId = LoginHelper.getUserId();
                if (ObjectUtil.isNotNull(userId)) {
                    baseEntity.setUpdatedBy(userId);
                }
            }
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    /**
     * 获取当前登录用户名
     */
    private LoginUser getLoginUser() {
        LoginUser loginUser;
        try {
            loginUser = LoginHelper.getLoginUser();
        } catch (Exception e) {
            log.warn("自动注入警告 => 用户未登录");
            return null;
        }
        return loginUser;
    }
}
