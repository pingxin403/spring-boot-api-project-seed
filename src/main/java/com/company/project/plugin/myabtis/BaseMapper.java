package com.company.project.plugin.myabtis;


import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 公有Mapper
 *
 **/
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    // 特别注意，该接口不能被扫描到，否则会出错



    /**
     * 以下定义的 4个 default method,
     * copy from {@link com.baomidou.mybatisplus.extension.toolkit.ChainWrappers}
     */
    default QueryChainWrapper<T> queryChain() {
        return new QueryChainWrapper<>(this);
    }

    default LambdaQueryChainWrapper<T> lambdaQueryChain() {
        return new LambdaQueryChainWrapper<>(this);
    }

    default UpdateChainWrapper<T> updateChain() {
        return new UpdateChainWrapper<>(this);
    }

    default LambdaUpdateChainWrapper<T> lambdaUpdateChain() {
        return new LambdaUpdateChainWrapper<>(this);
    }


    // 批量插入
    int insertBatchSomeColumn(List<T> entityList);
    // 批量更新
    int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);
    // 批量删除
    int deleteByIdWithFill(T entity);
}

