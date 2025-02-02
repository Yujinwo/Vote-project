package com.react.voteproject.exception;

import com.react.voteproject.utility.ResponseHelper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// 모든 컨트롤러에서 발생하는 예외를 처리하는 전역 예외 처리기
@RestControllerAdvice
public class GlobalExceptionHandler {
    // dto @Valid 검증 오류 메세지 반환
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("fail");
        Map<String, String> errorjson = new HashMap<>();
        errorjson.put("result",errorMessage);
        return errorjson;
    }
    // param @Valid 검증 오류 메세지 반환
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(ConstraintViolationException e) {
        Map<String, String> errorjson = new HashMap<>();
        String errorMessages = e.getConstraintViolations().stream()
                .map(violation -> violation.getMessage()) // 오류 메시지 부분만 추출
                .collect(Collectors.joining("; "));
        errorjson.put("result",errorMessages);
        return errorjson;
    }

    @ExceptionHandler(CreationException.class)
    public ResponseEntity<Map<Object, Object>> handleCreationException(CreationException e) {
        return ResponseHelper.createErrorMessage("result",e.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<Object, Object>> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseHelper.createErrorMessage("result",e.getMessage(),HttpStatus.UNAUTHORIZED);
    }

}
