package com.react.voteproject.context;


import com.react.voteproject.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthContext {


    public static User getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal(); // 캐스팅하여 User 정보 가져오기
        }
        return null;
    }

    // 유저 로그인 인증 함수
    public static Boolean checkAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return true;
        }
        return false;
    }
    public static void deleteAuth() {
        SecurityContextHolder.clearContext();
    }



}
