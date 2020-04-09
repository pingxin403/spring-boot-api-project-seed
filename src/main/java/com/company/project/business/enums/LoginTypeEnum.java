package com.company.project.business.enums;

import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.business.enums
 * hyp create at 20-4-4
 **/
public enum LoginTypeEnum implements BaseCodeEnum {
    /**
     *
     */
    WEB(1, "web"), APP(2, "app");
    int code;
    String name;

    LoginTypeEnum(int code, String name) {
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
