package com.react.voteproject.dto;


import com.react.voteproject.context.AuthContext;
import com.react.voteproject.entity.Comment;
import com.react.voteproject.entity.User;
import com.react.voteproject.entity.Vote;
import com.react.voteproject.repository.CommentRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentWriteDto {

    private Long vote_id;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 3,max = 280,message = "최소 3자 이상, 최대 280자 이하로 입력해주세요")
    private String content;

    public Comment createComment(Vote vote, User user)
    {
        return Comment.builder()
                .content(content)
                .user(user)
                .vote(vote)
                .build();

    }


}
