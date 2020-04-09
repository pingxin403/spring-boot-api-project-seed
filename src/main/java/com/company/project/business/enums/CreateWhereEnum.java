package com.company.project.business.enums;

import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.business.enums
 * hyp create at 20-4-4
 **/
public enum CreateWhereEnum implements BaseCodeEnum {

    /**
     *
     */
    web(1,"web"),android(2,"安卓"),ios(3,"ios");
    int code;
    String name;

    CreateWhereEnum(int code, String msg) {
        this.code = code;
        this.name = msg;
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
