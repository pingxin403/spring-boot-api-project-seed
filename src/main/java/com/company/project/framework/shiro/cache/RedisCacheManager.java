package com.company.project.framework.shiro.cache;


import com.company.project.business.service.RedisService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisCacheManager implements CacheManager {

    @Autowired
    private RedisService redisService;


    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new RedisCache(name, redisService);
    }

}