package com.company.project.business.enums;

import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * 日志类型
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.business.enums
 * hyp create at 20-3-21
 **/

public enum LogTypeEnum implements BaseCodeEnum {
    /**
     * 系统
     */
    SYSTEM(1, "系统"), VISIT(2, "游客"), ERROR(3, "异常");

    int code;
    String name;

    LogTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return name;
    }
}
