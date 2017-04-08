package com.orangex.care.utils;

/**
 * Created by orangex on 2017/3/19.
 */

public class ObjectUtil {
    public static <T> T checkIsNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }
}
