package com.react.voteproject.dto;


import com.react.voteproject.category.category_enum;
import com.react.voteproject.context.AuthContext;
import com.react.voteproject.entity.Vote;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoteWriteDto {

    @NotBlank(message = "제목을 입력해주세요")
    @Size(min =5,max = 20,message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "카테고리를 설정해주세요")
    private String category;

    @Size(min =2,max = 2,message = "날짜를 설정해주세요")
    private List<String> days;

    @Size(min =2,max = 2,message = "선택지 2개를 설정해주세요")
    private List<String> choices;


    public Vote createVote() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDay = LocalDateTime.parse(days.get(0), formatter);
        LocalDateTime endDay = LocalDateTime.parse(days.get(1), formatter);

        return Vote.builder().user(AuthContext.getAuth()).title(title).category(category).startDay(startDay).endDay(endDay).build();
    }
}
