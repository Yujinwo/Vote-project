package com.react.voteproject.repository;

import com.react.voteproject.entity.User;
import com.react.voteproject.entity.Vote;
import com.react.voteproject.repository.querydsl.VoteRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VoteRepository extends JpaRepository<Vote,Long>, VoteRepositoryCustom {


    @Query("select v from Vote v WHERE category = :category")
    Slice<Vote> findAllByVoteAndCategory(Pageable pageable, @Param("category") String category);

    @Query("select v from Vote v")
    Slice<Vote> findAllByVote(Pageable pageable);

    Page<Vote> findByuser(Pageable pageable, User user);

    @Query("SELECT v, SUM(vo.count) AS totalCount " +
            "FROM Vote v " +
            "LEFT JOIN VoteOption vo ON vo.vote.id = v.id " +
            "WHERE v.category = :category " +
            "GROUP BY v.id " +
            "ORDER BY totalCount DESC")
    Slice<Object[]> findVotesWithTotalCountByCategory(Pageable pageable,@Param("category") String category);

    @Query("SELECT v, SUM(vo.count) AS totalCount " +
            "FROM Vote v " +
            "LEFT JOIN VoteOption vo ON vo.vote.id = v.id " +
            "GROUP BY v.id " +
            "ORDER BY totalCount DESC")
    Slice<Object[]> findVoteWithTotalCount(Pageable pageable);
}
