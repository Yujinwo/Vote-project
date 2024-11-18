package com.react.voteproject.entity;

import com.react.voteproject.context.AuthContext;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@BatchSize(size = 10)
public class VoteOption extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @OneToMany(mappedBy = "voteOption", fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    private List<UserVote> uservotes;
    // 좋아요
    public void upCount() {
        count = count + 1;
    }
    // 좋아요 취소
    public void downCount() {
        if(count > 0)
        {
            count = count - 1;
        }
    }
    // 투표 선택지 내용 수정
    public void changeContent(String content) {
      this.content = content;
    }

    public UserVote createUserVote(){
        return UserVote.builder()
                .voteOption(this)
                .vote(vote)
                .user(AuthContext.getAuth())
                .build();
    }
}
