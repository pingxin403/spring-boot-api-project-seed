package com.company.project.business.enums;

import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * 配置的key
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.business.enums
 * hyp create at 20-3-19
 **/
public enum ConfigKeyEnum implements BaseCodeEnum {
    /**
     * 站点简介
     */
    SITE_DESC(1, "siteDesc"),
    /**
     * 主域名
     */
    DOMAIN(2, "domain"),
    /**
     * 站点地址
     */
    SITE_URL(3, "siteUrl"),
    /**
     * 站点图标
     */
    SITE_FAVICON(4, "siteFavicon"),
    /**
     * 资源文件域名
     */
    STATIC_WEB_SITE(5, "staticWebSite"),
    /**
     * CMS后管系统地址
     */
    CMS_URL(6, "cmsUrl"),

    /**
     * 网站Title
     */
    SITE_NAME(7, "siteName"),
    /**
     * 网站首页的Description
     */
    HOME_DESC(8, "homeDesc"),
    /**
     * 网站首页的Keywords
     */
    HOME_KEYWORDS(9, "homeKeywords"),
    /**
     * 站长名称
     */
    AUTHOR_NAME(10, "authorName"),
    /**
     * 站长邮箱
     */
    AUTHOR_EMAIL(11, "authorEmail"),

    /**
     * 系统最后一次更新的日期
     */
    UPDATE_TIME(12, "updateTime"),

    /**
     * 网站安装时间，默认为执行init_data.sql的时间
     */
    INSTALLDATE(13, "installdate"),

    /**
     * 当切换浏览器tab时，在原tab上的标题。比如https://www.zhyd.me上的“麻溜儿回来~~~”
     */
    DYNAMIC_TITLE(14, "dynamicTitle"),
    ;

    int code;
    String name;

    ConfigKeyEnum(int code, String msg) {
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
