package com.react.voteproject.dto;


import com.react.voteproject.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinDto {
    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 4,max = 10,message = "최소 4자 이상, 최대 10자 이하로 입력해주세요")
    private String user_id;
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 7,max = 15,message = "최소 7자 이상, 최대 15자 이하로 입력해주세요")
    private String user_pw;
    @NotBlank(message = "재확인 비밀번호를 입력해주세요")
    @Size(min = 7,max = 15,message = "최소 7자 이상, 최대 15자 이하로 입력해주세요")
    private String user_confirmpw;
    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2,max = 6,message = "최소 2자 이상, 최대 6자 이하로 입력해주세요")
    private String user_nick;

    public User createUser() {

        return User.builder()
                .userId(user_id)
                .userPw(user_pw)
                .userNick(user_nick)
                .build();
    }

}
