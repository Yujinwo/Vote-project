package com.react.voteproject.dto;


import com.react.voteproject.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;
    private String content;
    private String created_date;
    private UserDto user;

    public static CommentResponseDto createCommentResponseDto(Comment comment){

        // 변환 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 원하는 형식으로 변환
        String formattedDate = comment.getCreatedDate().format(formatter);

        return CommentResponseDto.builder().id(comment.getId()).content(comment.getContent()).created_date(formattedDate).user(UserDto.createUserDto(comment.getUser())).build();

    }

}
