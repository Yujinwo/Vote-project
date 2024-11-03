package com.react.voteproject.entity;


import com.react.voteproject.category.category_enum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Vote extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @Column
    private String category;

    @Column
    private int up;


    @Column
    private LocalDateTime startDay;

    @Column
    private LocalDateTime endDay;

    @OneToMany(mappedBy = "vote",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    private List<VoteOption> options;

    @OneToMany(mappedBy = "vote",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Comment> comments;

    @PrePersist
    public void prePersist() {
        if (up == 0) up = 0;
        if (startDay == null) startDay = LocalDateTime.now();
        if (endDay == null) endDay = LocalDateTime.now().plusDays(7);
    }

    // 투표 수정
    public void changeVoteContent(String title,String category,LocalDateTime startDay,LocalDateTime endDay) {
        this.title = title;
        this.category = category;
        this.startDay = startDay;
        this.endDay = endDay;
    }

    // 좋아요
    public void changeUp( int count) {

        this.up = up + count;
    }
}
