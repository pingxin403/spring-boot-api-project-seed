package com.company.project.business.enums;

import com.company.project.framework.mybatis.BaseCodeEnum;

/**
 * 缓存Key前缀
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.business.enums
 * hyp create at 20-3-21
 **/
public enum CachePrefixEnum implements BaseCodeEnum {

    /**
     * 页面
     */
    VIEW(1, "view_cache_"),
    /**
     * 防DDOS
     */
    DDOS(2, "ddos_cache_"),
    /**
     *
     */
    WX(3, "wx_api_cache_"),
    /**
     * 爬虫
     */
    SPIDER(4, "spider_cache_"),
    ;
    int code;
    String name;

    CachePrefixEnum(int code, String msg) {
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
