package com.react.voteproject.entity;


import com.react.voteproject.category.category_enum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Enumerated(EnumType.STRING)
    private category_enum category;

    @Column
    private int up;

    @Column
    private int commentCount;

    @Column
    private LocalDateTime startDay;

    @Column
    private LocalDateTime endDay;
}
