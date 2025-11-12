// src/main/java/com/example/demo/exception/GlobalExceptionHandler.java
package com.example.demo.exception;

import com.example.demo.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 모든 @RestController에서 발생하는 예외를 이 클래스가 처리하도록 설정
public class GlobalExceptionHandler {

    /**
     * 잘못된 인자값(Argument)이 들어온 경우에 대한 처리
     * 예: 존재하지 않는 ID로 조회를 시도
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponseDto response = new ErrorResponseDto(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request
    }

    /**
     * 비즈니스 로직상 허용되지 않는 상태(State)에 대한 처리
     * 예: 이미 예매된 좌석을 다시 예매하려고 시도
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalStateException(IllegalStateException ex) {
        ErrorResponseDto response = new ErrorResponseDto(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409 Conflict
    }
}