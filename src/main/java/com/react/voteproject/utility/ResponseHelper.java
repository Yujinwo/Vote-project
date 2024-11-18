package com.react.voteproject.utility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
public class ResponseHelper {

        public static ResponseEntity<Map<Object, Object>> createErrorMessage(String key, Object message) {
            Map<Object, Object> errorResult = new HashMap<>();
            errorResult.put(key, message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }

        public static ResponseEntity<Map<Object, Object>> createSuccessMessage(String key, Object data) {
            Map<Object, Object> successResult = new HashMap<>();
            successResult.put(key, data);
            return ResponseEntity.status(HttpStatus.OK).body(successResult);
        }

}
