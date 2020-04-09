package com.company.project.business.enums;

import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * 日志级别
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.business.enums
 * hyp create at 20-3-21
 **/
public enum LogLevelEnum implements BaseCodeEnum {
    /**
     * 错误
     */
    ERROR(1, "error"), WARN(2, "warn"), INFO(3, "info"), DEBUG(4, "debug");

    int code;
    String name;

    LogLevelEnum(int code, String name) {
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
