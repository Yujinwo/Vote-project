package com.react.voteproject.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {


    @NotBlank
    @Size(min = 4,max = 10,message = "최소 4자 이상, 최대 10자 이하로 입력해주세요")
    private String user_id;

    @NotBlank
    @Size(min = 7,max = 15,message = "최소 7자 이상, 최대 15자 이하로 입력해주세요")
    private String user_pw;

}
