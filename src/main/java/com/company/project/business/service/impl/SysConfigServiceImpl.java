package com.company.project.business.service.impl;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.business.annotation.RedisCache;
import com.company.project.business.consts.DateConst;
import com.company.project.business.enums.ConfigKeyEnum;
import com.company.project.business.service.ISysConfigService;
import com.company.project.framework.property.AppProperties;
import com.company.project.persistence.beans.SysConfig;
import com.company.project.persistence.mapper.SysConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置
 */
@Slf4j
@Service
public class SysConfigServiceImpl implements ISysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Autowired
    private AppProperties properties;

    /**
     * 获取系统配置
     *
     * @return
     */
    @Override
    @RedisCache(enable = true)
    public Map<String, Object> getConfigs() {
        List<SysConfig> list = sysConfigMapper.selectList(Wrappers.emptyWrapper());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        String updateTimeKey = ConfigKeyEnum.UPDATE_TIME.getMsg();
        Map<String, Object> res = new HashMap<>();
        res.put(updateTimeKey, DateUtil.parse("2019-01-01 00:00:00", DateConst.YYYY_MM_DD_HH_MM_SS_EN));
        for (SysConfig config : list) {
            res.put(config.getConfigKey(), config.getConfigValue());
        }

        return res;
    }


    @Override
    @RedisCache(flush = true, enable = true)
    public void saveConfig(String key, String value) {
        if (!StringUtils.isEmpty(key)) {
            SysConfig config = null;
            if (null == (config = this.getByKey(key))) {
                config = new SysConfig();
                config.setConfigKey(key);
                config.setConfigValue(value);
                config.setCreateTime(LocalDateTime.now());
                config.setUpdateTime(LocalDateTime.now());
                this.sysConfigMapper.insert(config);
            } else {
                config.setConfigKey(key);
                config.setConfigValue(value);
                config.setUpdateTime(LocalDateTime.now());
                this.sysConfigMapper.updateById(config);
            }
        }
    }

    @Override
    @RedisCache(enable = true)
    public SysConfig getByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        SysConfig sysConfig = new SysConfig();
        sysConfig.setConfigKey(key);
        return this.sysConfigMapper.selectOne(Wrappers.query(sysConfig));
    }

    /**
     * 添加/修改系统配置
     *
     * @param configs 所有的配置项
     */
    @Override
    @RedisCache(flush = true, enable = true)
    public void saveConfig(Map<String, String> configs) {
        if (!CollectionUtils.isEmpty(configs)) {
            configs.forEach(this::saveConfig);
        }
    }

    @Override
    @RedisCache(enable = true)
    public String getSpiderConfig() {
        SysConfig config = this.getByKey("spiderConfig");
        if (config == null) {
            return "{}";
        }
        return StringUtils.isEmpty(config.getConfigValue()) ? "{}" : config.getConfigValue();
    }

    @Override
    public List<String> getRandomUserAvatar() {
        SysConfig config = this.getByKey("defaultUserAvatar");
        if (config == null) {
            return null;
        }
        try {
            return JSONArray.parseArray(config.getConfigValue(), String.class);
        } catch (Exception e) {
            log.error("配置项无效！defaultUserAvatar = [" + config.getConfigValue() + "]");
        }
        return null;
    }
}
