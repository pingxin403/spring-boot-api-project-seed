package com.company.project.business.enums;

import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * 平台
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.business.enums
 * hyp create at 20-3-19
 **/
public enum PlatformEnum implements BaseCodeEnum {
    /**
     * 管理平台
     */
    ADMIN(1, "管理平台"),
    /**
     * web平台
     */
    WEB(2, "web平台");

    int code;
    String name;


    PlatformEnum(int code, String name) {
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
