package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Configuration
public class RedisConfig {

    /**
     * 🔥 1) Redis 서버 연결 설정 (중요)
     * docker-compose.yml 에서 Redis 컨테이너 이름이 'myredis' 이므로
     * hostName = "myredis" 로 설정해야 모든 서비스가 동일한 Redis를 사용함.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("myredis", 6379);
    }

    /**
     * 🔥 2) ObjectMapper 빈 등록 → Security 모듈 + DefaultTyping 활성화
     * 두 서비스의 직렬화 설정을 100% 동일하게 맞추는 핵심
     */
    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        return mapper;
    }

    /**
     * 🔥 3) Redis 세션 직렬화기 등록
     * spring-session이 이 Serializer를 사용하여 SecurityContext 저장/복원함.
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(ObjectMapper redisObjectMapper) {
        return new GenericJackson2JsonRedisSerializer(redisObjectMapper);
    }
}
