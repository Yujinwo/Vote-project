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
    private int commentCount;

    @Column
    private LocalDateTime startDay;

    @Column
    private LocalDateTime endDay;

    @OneToMany(mappedBy = "vote",fetch = FetchType.LAZY)
    private List<VoteOption> options;

    @PrePersist
    public void prePersist() {
        if (up == 0) up = 0;
        if (commentCount == 0) commentCount = 0;
        if (startDay == null) startDay = LocalDateTime.now();
        if (endDay == null) endDay = LocalDateTime.now().plusDays(7);
    }
}
