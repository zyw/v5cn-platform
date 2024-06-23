package cn.v5cn.platform.framework.core.utils;

import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * sql操作工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlUtils {

    /**
     * 定义常用的sql关键字
     */
    private static final String SQL_REGEX = "select |insert |delete |update |drop |count |exec |chr |mid |master |truncate |char |and |declare ";

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    private static final String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value) {
        if (StrUtil.isNotEmpty(value) && !isValidOrderBySql(value)) {
            throw new IllegalArgumentException("参数不符合规范，不能进行查询");
        }
        return value;
    }

    /**
     * 验证order by语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

    /**
     * SQL关键字检查
     */
    public static void filterKeyword(String value) {
        if (StrUtil.isEmpty(value)) {
            return;
        }
        List<String> sqlKeywords = StrUtil.split(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords) {
            if (StrUtil.indexOfIgnoreCase(value, sqlKeyword) > -1) {
                throw new IllegalArgumentException("参数存在SQL注入风险");
            }
        }
    }
}
