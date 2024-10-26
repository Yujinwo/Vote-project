package com.react.voteproject.utility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
public class ResponseHelper {

        // 공통적으로 사용할 에러 메시지 생성 메서드
        public static ResponseEntity<Map<String, Object>> createErrorMessage(String key, Object message) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put(key, message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }

        // 성공 메시지나 다른 타입의 응답도 추가 가능
        public static ResponseEntity<Map<String, Object>> createSuccessMessage(String key, Object data) {
            Map<String, Object> successResult = new HashMap<>();
            successResult.put(key, data);
            return ResponseEntity.status(HttpStatus.OK).body(successResult);
        }

}
