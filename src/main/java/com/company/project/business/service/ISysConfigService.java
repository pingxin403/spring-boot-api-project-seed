package com.company.project.business.service;


import com.company.project.persistence.beans.SysConfig;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 系统配置
 */
public interface ISysConfigService {

    /**
     * 获取系统配置
     *
     * @return
     */
    Map<String, Object> getConfigs();

    /**
     * 添加/修改系统配置
     *
     * @param configs 所有的配置项
     */
    void saveConfig(Map<String, String> configs);

    /**
     * 添加/修改单个
     *
     * @param key   key
     * @param value value
     */
    void saveConfig(String key, String value);

    /**
     * 获取单个配置
     *
     * @param key key
     */
    SysConfig getByKey(String key);
    /**
     * 返回 Spider
     */
    String getSpiderConfig();

    /**
     * 获取随机的用户头像
     */
    List<String> getRandomUserAvatar();

}
