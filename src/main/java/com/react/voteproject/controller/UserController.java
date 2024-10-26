package com.react.voteproject.controller;


import com.react.voteproject.dto.UserJoinDto;
import com.react.voteproject.dto.UserLoginDto;
import com.react.voteproject.entity.User;
import com.react.voteproject.service.UserService;
import com.react.voteproject.utility.ResponseHelper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class UserController {
    
    private final UserService userService;
    // 영어와 숫자만 허용하는 정규식
    private static final Pattern alphanumericPattern = Pattern.compile("^[a-zA-Z0-9]*$");
    // 한글 초성을 확인하는 정규식
    private static final Pattern koreanInitialPattern = Pattern.compile("^[ㄱ-ㅎ]*$");

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> Login(@Valid @RequestBody UserLoginDto userLoginDto) {
        if(!alphanumericPattern.matcher(userLoginDto.getUser_id()).matches())
        {
            return ResponseHelper.createErrorMessage("result","알파벳, 숫자 조합으로 입력해주세요");
        }
        Optional<User> user = userService.login(userLoginDto);
        if(user.isPresent())
        {
            return ResponseHelper.createSuccessMessage("result","로그인 성공");
        }

        return ResponseHelper.createErrorMessage("result","아이디와 비밀번호를 확인해주세요");
        
    }
    @GetMapping("/users")
    public ResponseEntity<Map<String,Object>> GetUserId(@RequestParam("user_id") @NotBlank @Size(min = 4, max = 10,message = "최소 4자 이상, 최대 10자 이하로 입력해주세요") String user_id) {
        if(!alphanumericPattern.matcher(user_id).matches())
        {
            return ResponseHelper.createErrorMessage("result","알파벳, 숫자 조합으로 입력해주세요");
        }
        Optional<User> user = userService.getUserId(user_id);
        if(user.isPresent()){
            return ResponseHelper.createErrorMessage("result","존재 하는 id 입니다");
        }
        return ResponseHelper.createSuccessMessage("result","사용할 수 있는 id 입니다");

    }

    @PostMapping("/users")
    public ResponseEntity<Map<String,Object>> Join(@Valid @RequestBody UserJoinDto userJoinDto) {
        if(!alphanumericPattern.matcher(userJoinDto.getUser_id()).matches())
        {
            return ResponseHelper.createErrorMessage("result","알파벳, 숫자 조합으로 입력해주세요");
        }
        if(koreanInitialPattern.matcher(userJoinDto.getUser_nick()).matches()){

            return ResponseHelper.createErrorMessage("result","한글 초성은 불가능합니다");
        }
        // id 중복 확인
        Optional<User> userId = userService.getUserId(userJoinDto.getUser_id());
        if (userId.isPresent())
        {
            return ResponseHelper.createErrorMessage("result","존재하는 ID가 있습니다");
        }

        // 비밀번호와 재확인 비밀번호 일치 확인
        if(!userJoinDto.getUser_pw().equals(userJoinDto.getUser_confirmpw()))
        {
            return ResponseHelper.createErrorMessage("result","비밀번호를 일치 시켜 주세요");
        }

        Optional<User> user = userService.join(userJoinDto);
        if(user.isPresent())
        {
            return ResponseHelper.createSuccessMessage("result","회원가입 성공");
        }

        return ResponseHelper.createErrorMessage("result","회원가입 실패");

    }
}
