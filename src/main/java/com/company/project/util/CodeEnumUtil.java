package com.company.project.util;


import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * @author hyp
 * Project name is equipment-management
 * Include in com.hyp.ujs.em.utils
 * hyp create at 20-1-4
 **/
public class CodeEnumUtil {

    public static <E extends Enum<?> & BaseCodeEnum> E codeOf(Class<E> enumClass, String msg) {
        E[] enumConstants = enumClass.getEnumConstants();
        for (E e : enumConstants) {
            if (e.getMsg().equals(msg)) {
                return e;
            }
        }
        return null;
    }

    public static <E extends Enum<?> & BaseCodeEnum> E codeOf(Class<E> enumClass, int code) {
        E[] enumConstants = enumClass.getEnumConstants();
        for (E e : enumConstants) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
