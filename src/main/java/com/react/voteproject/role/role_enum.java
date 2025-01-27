package com.react.voteproject.role;


import lombok.Getter;

@Getter
public enum role_enum {
    USER("Role_USER"),
    ADMIN("Role_ADMIN");

    private String role;

    role_enum(String role){
        this.role = role;
    }

}
