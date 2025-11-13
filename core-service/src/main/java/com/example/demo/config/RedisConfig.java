package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
// Spring Security의 Jackson 모듈을 import 합니다.
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Configuration
public class RedisConfig {

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        // 1. 기본 Jackson ObjectMapper를 생성합니다.
        ObjectMapper mapper = new ObjectMapper();

        // 2. Spring Security의 특별한 객체들(예: DefaultSavedRequest)을
        //    처리할 수 있도록 Security 모듈을 등록합니다.
        //    (이것이 500 오류를 해결하는 핵심입니다.)
        mapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));

        // 3. Spring Security 모듈이 등록된 ObjectMapper를 사용하여
        //    새로운 JSON 직렬화기를 생성합니다.
        return new GenericJackson2JsonRedisSerializer(mapper);
    }
}
