package com.react.voteproject.repository.querydsl.lmpl;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.react.voteproject.dto.VoteWithCommentCountDTO;
import com.react.voteproject.entity.*;
import com.react.voteproject.repository.querydsl.VoteRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;



@RequiredArgsConstructor
public class VoteRepositoryImpl implements VoteRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<VoteWithCommentCountDTO> findVotesWithCommentCount(Pageable pageable,String user_id) {

        QVote vote = QVote.vote;
        QComment comment = QComment.comment;

        // 서브쿼리를 사용해 댓글 수를 함께 가져옴
        JPAQuery<VoteWithCommentCountDTO> query = jpaQueryFactory
                .select(Projections.constructor(
                        VoteWithCommentCountDTO.class,
                        vote.id,
                        vote.title,
                        vote.up,
                        comment.count()
                ))
                .from(vote)
                .where(vote.user.userId.eq(user_id))
                .leftJoin(comment).on(comment.vote.id.eq(vote.id)).fetchJoin()
                .groupBy(vote.id);

        // 페이징 처리
        List<VoteWithCommentCountDTO> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 데이터 수
        long total = jpaQueryFactory.select(vote.count())
                .from(vote)
                .where(vote.user.userId.eq(user_id))
                .fetchOne();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);

    }

    @Override
    public Page<VoteWithCommentCountDTO> findUserVotesWithCommentCount(Pageable pageable,String user_id) {

        QVote vote = QVote.vote;
        QComment comment = QComment.comment;
        QUserVote userVote = QUserVote.userVote;

        // 서브쿼리를 사용해 댓글 수를 함께 가져옴
        JPAQuery<VoteWithCommentCountDTO> query = jpaQueryFactory
                .select(Projections.constructor(
                        VoteWithCommentCountDTO.class,
                        userVote.vote.id,
                        userVote.vote.title,
                        userVote.vote.up,
                        comment.count()
                ))
                .from(userVote)
                .where(userVote.vote.user.userId.eq(user_id))
                .leftJoin(comment).on(comment.vote.id.eq(userVote.vote.id)).fetchJoin()
                .groupBy(userVote.vote.id);

        // 페이징 처리
        List<VoteWithCommentCountDTO> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 데이터 수
        long total = jpaQueryFactory.select(userVote.count())
                .from(userVote)
                .where(userVote.vote.user.userId.eq(user_id))
                .fetchOne();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);

    }

    @Override
    public Page<VoteWithCommentCountDTO> findUpVotesWithCommentCount(Pageable pageable,String user_id) {

        QVote vote = QVote.vote;
        QComment comment = QComment.comment;
        QUp up = QUp.up;

        // 서브쿼리를 사용해 댓글 수를 함께 가져옴
        JPAQuery<VoteWithCommentCountDTO> query = jpaQueryFactory
                .select(Projections.constructor(
                        VoteWithCommentCountDTO.class,
                        up.vote.id,
                        up.vote.title,
                        up.vote.up,
                        comment.count()
                ))
                .from(up)
                .where(up.vote.user.userId.eq(user_id))
                .leftJoin(comment).on(comment.vote.id.eq(up.vote.id)).fetchJoin()
                .groupBy(up.vote.id);

        // 페이징 처리
        List<VoteWithCommentCountDTO> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 데이터 수
        long total = jpaQueryFactory.select(up.count())
                .from(up)
                .where(up.vote.user.userId.eq(user_id))
                .fetchOne();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);

    }

    @Override
    public Page<VoteWithCommentCountDTO> findBookmarkVotesWithCommentCount(Pageable pageable,String user_id) {

        QVote vote = QVote.vote;
        QComment comment = QComment.comment;
        QBookmark bookmark = QBookmark.bookmark;

        // 서브쿼리를 사용해 댓글 수를 함께 가져옴
        JPAQuery<VoteWithCommentCountDTO> query = jpaQueryFactory
                .select(Projections.constructor(
                        VoteWithCommentCountDTO.class,
                        bookmark.vote.id,
                        bookmark.vote.title,
                        bookmark.vote.up,
                        comment.count()
                ))
                .from(bookmark)
                .where(bookmark.vote.user.userId.eq(user_id))
                .leftJoin(comment).on(comment.vote.id.eq(bookmark.vote.id)).fetchJoin()
                .groupBy(bookmark.vote.id);

        // 페이징 처리
        List<VoteWithCommentCountDTO> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 데이터 수
        long total = jpaQueryFactory.select(bookmark.count())
                .from(bookmark)
                .where(bookmark.vote.user.userId.eq(user_id))
                .fetchOne();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);

    }

}
