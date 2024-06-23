package cn.v5cn.platform.framework.log.event;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志记录事件
 */
@Data
public class OperationLogEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    private Long operationId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 操作模块
     */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    private Integer businessType;

    /**
     * 业务类型数组
     */
    private Integer[] businessTypes;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    private Integer operatorType;

    /**
     * 操作人员
     */
    private String operationName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 请求url
     */
    private String operationUrl;

    /**
     * 操作地址
     */
    private String operationIp;

    /**
     * 操作地点
     */
    private String operationLocation;

    /**
     * 请求参数
     */
    private String operationParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 消耗时间
     */
    private Long costTime;
}
