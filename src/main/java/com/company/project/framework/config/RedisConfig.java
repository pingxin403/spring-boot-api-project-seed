package com.company.project.framework.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 缓存数据时Key的生成器，可以依据业务和技术场景自行定制
     *
     * @return
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator() {

        return (target, method, parame) -> {
            StringBuilder sb = new StringBuilder();
            //类名+方法名+参数
            sb.append(target.getClass().getName())
                    .append(".")
                    .append(method.getName());
            for (Object p : parame) {
                sb.append(p);
            }
            return sb.toString();
        };
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {

        return RedisCacheManager.builder(
                RedisCacheWriter
                        .nonLockingRedisCacheWriter(factory)
        ).cacheDefaults(
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(
                                Duration.ofDays(30)
                        )
        )
                .build();
    }

    /**
     * 自定义templates
     * see: https://blog.csdn.net/weixin_43549578/article/details/84821986
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
