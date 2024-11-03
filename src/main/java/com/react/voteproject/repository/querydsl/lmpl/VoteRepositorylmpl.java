package com.react.voteproject.repository.querydsl.lmpl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.react.voteproject.repository.querydsl.VoteRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;

@RequiredArgsConstructor
public class VoteRepositorylmpl implements VoteRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
}
