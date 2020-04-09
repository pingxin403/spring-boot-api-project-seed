package com.company.project.business.enums;

import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.business.enums
 * hyp create at 20-4-4
 **/
public enum  GenderEnum implements BaseCodeEnum {
    /**
     *
     */
    male(1,"男"),female(2,"女");
    int code;
    String name;

    GenderEnum(int code, String msg) {
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
