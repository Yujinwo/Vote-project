package com.react.voteproject.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;
import com.react.voteproject.category.category_enum;
import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.*;
import com.react.voteproject.entity.User;
import com.react.voteproject.jwt.JwtProvider;
import com.react.voteproject.repository.UserRepository;
import com.react.voteproject.service.RefreshTokenService;
import com.react.voteproject.service.UserService;
import com.react.voteproject.utility.ResponseHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class UserController {
    
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    // 영어와 숫자만 허용하는 정규식
    private static final Pattern alphanumericPattern = Pattern.compile("^[a-zA-Z0-9]*$");
    // 한글 초성을 확인하는 정규식
    private static final Pattern koreanInitialPattern = Pattern.compile("^[ㄱ-ㅎ]*$");

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> Login(@Valid @RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
        Map<String,Object> result = new HashMap<>();
        if(!alphanumericPattern.matcher(userLoginDto.getUser_id()).matches())
        {
            result.put("result","알파벳, 숫자 조합으로 입력해주세요");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        LoginResponseDTO user = userService.login(userLoginDto);

        if(user.getAccessToken() != null)
        {
            result.put("result","로그인 성공");
            result.put("accessToken",user.getAccessToken());
            result.put("refreshToken",user.getRefreshToken());
            // Refresh Token을 쿠키에 저장
            Cookie refreshCookie = new Cookie("refreshToken", user.getRefreshToken());
            refreshCookie.setHttpOnly(true);       // XSS 공격 방지
            refreshCookie.setSecure(true);        // HTTPS에서만 작동 (개발 시에는 false로 설정 가능)
            refreshCookie.setPath("/");           // 모든 경로에서 사용 가능
            refreshCookie.setMaxAge(24 * 60 * 60); // 24시간 (초 단위)

            response.addCookie(refreshCookie);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        result.put("result","아이디와 비밀번호를 확인해주세요");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            Map<String,Object> result = new HashMap<>();
            result.put("errorMsg","인증이 정확하지 않습니다");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        RefreshTokenResponseDTO refreshTokenResponseDTO = refreshTokenService.refreshToken(refreshToken);
        String newAccessToken = refreshTokenResponseDTO.getAccessToken();

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<Object,Object>> Logout(HttpServletResponse response)
    {
        if(AuthContext.checkAuth()) {
            AuthContext.deleteAuth();
            Cookie cookie = new Cookie("refreshToken", null);
            cookie.setHttpOnly(true);  // JavaScript 접근 불가
            cookie.setSecure(true);    // HTTPS에서만 전송 (필요한 경우 설정)
            cookie.setPath("/");       // 쿠키 경로 설정 (모든 경로에 대해 유효)
            cookie.setMaxAge(0);       // 쿠키 만료 설정 (즉시 만료)

            response.addCookie(cookie);  // 클라이언트로 쿠키 전달
            return ResponseHelper.createSuccessMessage("result","로그아웃 성공");
        }
        return ResponseHelper.createErrorMessage("result","로그아웃 실패");
    }
    // 유저 로그인 여부 확인
    @GetMapping("/sessions")
    public ResponseEntity<Map<Object,Object>> getSession() {

        if(AuthContext.checkAuth())
        {
            return ResponseHelper.createSuccessMessage("result",AuthContext.getAuth().getUserId());
        }

        return ResponseHelper.createErrorMessage("result","세션 무효");
    }
    // id 조회
    @GetMapping("/users")
    public ResponseEntity<Map<Object,Object>> findUserId(@RequestParam("user_id") @NotBlank @Size(min = 4, max = 10,message = "최소 4자 이상, 최대 10자 이하로 입력해주세요") String user_id) {
        if(!alphanumericPattern.matcher(user_id).matches())
        {
            return ResponseHelper.createErrorMessage("result","알파벳, 숫자 조합으로 입력해주세요");
        }
        Optional<User> user = userService.findUserId(user_id);
        if(user.isPresent()){
            return ResponseHelper.createErrorMessage("result","존재 하는 id 입니다");
        }
        return ResponseHelper.createSuccessMessage("result","사용할 수 있는 id 입니다");

    }
    // 회원가입
    @PostMapping("/users")
    public ResponseEntity<Map<Object,Object>> Join(@Valid @RequestBody UserJoinDto userJoinDto) {
        if(!alphanumericPattern.matcher(userJoinDto.getUser_id()).matches())
        {
            return ResponseHelper.createErrorMessage("result","알파벳, 숫자 조합으로 입력해주세요");
        }
        if(koreanInitialPattern.matcher(userJoinDto.getUser_nick()).matches()){

            return ResponseHelper.createErrorMessage("result","한글 초성은 불가능합니다");
        }

        // id 중복 확인
        Optional<User> userId = userService.findUserId(userJoinDto.getUser_id());
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
            AuthContext.setAuth(user.get());
            return ResponseHelper.createSuccessMessage("result","회원가입 성공");
        }

        return ResponseHelper.createErrorMessage("result","회원가입 실패");

    }
    // My페이지 유저 통계 데이터 조회
    @GetMapping("/users/stats")
    public ResponseEntity<UserStatsDto> getuserstats() {

        if(!AuthContext.checkAuth())
        {
            return ResponseEntity.status(HttpStatus.OK).body(new UserStatsDto());
        }

        UserStatsDto userStatsDto = userService.getUserStats();
        return ResponseEntity.status(HttpStatus.OK).body(userStatsDto);
    }
    // 전체 유저 참여율 통계 데이터 조회
    @GetMapping("/uservotes/stats")
    public ResponseEntity<List<UserVoteStatsDto>> getuserVotestats(@RequestParam(value = "category",required = false) String category,@RequestParam(value = "day",defaultValue = "Thisyear") String day) throws JsonProcessingException {
        List<UserVoteStatsDto> userVoteStatsDto = userService.getVoteStats(category,day);
        return ResponseEntity.status(HttpStatus.OK).body(userVoteStatsDto);
    }
    // 유저 참여율 통계 데이터 id 조회
    @GetMapping("/uservotes/stats/search")
    public ResponseEntity<List<UserVoteStatsDto>> searchUserVotestats(@RequestParam(value = "id",required = false) String user_id,@RequestParam(value = "category",required = false) String category,@RequestParam(value = "day",defaultValue = "Thisyear") String day) {
        // id가 없다면
        if(user_id == null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        }
        List<UserVoteStatsDto> userVoteStatsDto = userService.searchVoteStats(user_id,category,day);
        return ResponseEntity.status(HttpStatus.OK).body(userVoteStatsDto);
    }

}
