package com.company.project.business.enums;

import org.springframework.util.StringUtils;

/**
 * 用户类型
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.business.enums
 * hyp create at 20-3-22
 **/
public enum UserTypeEnum {
    /**
     *
     */
    ROOT("超级管理员"),
    ADMIN("管理员"),
    USER("系统会员"),
    UNKNOW("未知");
    private String desc;

    UserTypeEnum(String desc) {
        this.desc = desc;
    }

    public static UserTypeEnum getByType(String type) {
        if (StringUtils.isEmpty(type)) {
            return UserTypeEnum.UNKNOW;
        }
        for (UserTypeEnum ut : UserTypeEnum.values()) {
            if (ut.toString().equalsIgnoreCase(type)) {
                return ut;
            }
        }
        return UserTypeEnum.UNKNOW;
    }

    public String getDesc() {
        return desc;
    }
}
