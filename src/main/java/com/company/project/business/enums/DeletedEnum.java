package com.company.project.business.enums;

import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.business.enums
 * hyp create at 20-4-4
 **/
public enum DeletedEnum implements BaseCodeEnum {
    /**
     *
     */
    deleted(0, "已删除"), notDeleted(1, "未删除");

    int code;
    String name;

    DeletedEnum(int code, String msg) {
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
