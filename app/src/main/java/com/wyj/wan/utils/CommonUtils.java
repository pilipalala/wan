package com.wyj.wan.utils;

/**
 * @author wangyujie
 * @date 2018/12/3.18:57
 * @describe 添加描述
 */
public class CommonUtils {
    /**
     * 泛型转换工具方法 eg:object ==> map<String, String>
     *
     * @param object Object
     * @param <T> 转换得到的泛型对象
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }
}
