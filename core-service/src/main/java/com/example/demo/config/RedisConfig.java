package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Configuration
public class RedisConfig {

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        // 1. 기본 Jackson ObjectMapper를 생성합니다.
        ObjectMapper mapper = new ObjectMapper();

        // 2. Spring Security의 특별한 객체들(예: DefaultSavedRequest)을
        //    처리할 수 있도록 Security 모듈을 등록합니다. (500 에러 방지)
        mapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));

        // 3. ✨ JSON에 클래스 타입 정보를 함께 저장합니다. ✨
        //    이것이 core-service가 User 객체를 인식하게 하는 핵심입니다.
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // 4. Spring Security 모듈과 타입 정보가 등록된 ObjectMapper를 사용하여
        //    새로운 JSON 직렬화기를 생성합니다.
        return new GenericJackson2JsonRedisSerializer(mapper);
    }
}
