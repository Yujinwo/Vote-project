package com.react.voteproject.context;


import com.react.voteproject.entity.User;

public class AuthContext {

    private static User user;

    public static User getAuth() {
        return user;
    }
    public static void setAuth(User user) {
        AuthContext.user = user;
    }
    // 유저 로그인 인증 함수
    public static Boolean checkAuth() {
        return AuthContext.user != null ? true : false;
    }
    public static void deleteAuth() {
        AuthContext.user = null;
    }



}
