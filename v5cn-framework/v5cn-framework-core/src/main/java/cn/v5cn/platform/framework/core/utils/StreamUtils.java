package cn.v5cn.platform.framework.core.utils;

import cn.hutool.core.collection.CollUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * stream 流工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamUtils {
    /**
     * 将collection拼接
     *
     * @param collection 需要转化的集合
     * @param function   拼接方法
     * @return 拼接后的list
     */
    public static <E> String join(Collection<E> collection, Function<E, String> function) {
        return join(collection, function, StringUtils.SEPARATOR);
    }

    /**
     * 将collection拼接
     *
     * @param collection 需要转化的集合
     * @param function   拼接方法
     * @param delimiter  拼接符
     * @return 拼接后的list
     */
    public static <E> String join(Collection<E> collection, Function<E, String> function, CharSequence delimiter) {
        if (CollUtil.isEmpty(collection)) {
            return StringUtils.EMPTY;
        }
        return collection.stream().map(function).filter(Objects::nonNull).collect(Collectors.joining(delimiter));
    }
}
