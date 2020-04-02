package com.company.project.plugin;


/**
 * 公有Mapper
 *
 **/
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    // 特别注意，该接口不能被扫描到，否则会出错
}

