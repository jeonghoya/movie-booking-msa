// src/main/java/com.example/demo/config/JacksonConfig.java
package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Hibernate의 지연 로딩된 프록시 객체를 Jackson이 처리할 수 있도록 모듈 등록
        objectMapper.registerModule(new Hibernate5JakartaModule());

        // 2. ✨ Java 8의 날짜/시간(LocalDateTime 등)을 처리하기 위한 모듈
        objectMapper.registerModule(new JavaTimeModule());

        // 3. ✨ LocalDateTime을 JSON으로 변환할 때 타임스탬프 형태가 아닌, ISO 8601 표준 문자열 형태로 출력하도록 설정
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }
}