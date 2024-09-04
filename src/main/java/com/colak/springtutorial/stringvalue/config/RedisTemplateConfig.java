package com.colak.springtutorial.stringvalue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        CompressRedis compressRedis = new CompressRedis();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);

        // For value use custom RedisSerializer
        redisTemplate.setValueSerializer(compressRedis);

        // redisTemplate.setHashKeySerializer(stringSerializer);
        // redisTemplate.setHashValueSerializer(compressRedis);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
