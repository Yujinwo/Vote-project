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

    public static Boolean checkAuth() {
        return AuthContext.user != null ? true : false;
    }
    public static void deleteAuth() {
        AuthContext.user = null;
    }



}
