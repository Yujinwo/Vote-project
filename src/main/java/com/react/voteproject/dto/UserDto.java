package com.react.voteproject.dto;


import com.react.voteproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String user_nick;
    private String user_id;

    public static UserDto createUserDto(User user) {


        return UserDto.builder().user_nick(user.getUserNick()).user_id(user.getUserId()).build();

    }

}
