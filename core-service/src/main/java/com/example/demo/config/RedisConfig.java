package com.example.demo.config; // 본인의 패키지 경로에 맞게 수정

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * Spring Session이 Redis에 세션을 저장할 때 사용할 Serializer를 JSON으로 설정합니다.
     * 이렇게 하면 모든 서비스가 동일한 JSON 방식으로 세션을 읽고 쓸 수 있습니다.
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}