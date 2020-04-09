package com.company.project.business.enums;

import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.business.enums
 * hyp create at 20-4-4
 **/
public enum PermissionTypeEnum implements BaseCodeEnum {
    /**
     *
     */
    DIRECTORY(1, "目录"), MENU(2, "菜单"), BUTTON(3, "按钮"), REST_API(4, "REST-API");
    int code;
    String name;

    PermissionTypeEnum(int code, String name) {
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
