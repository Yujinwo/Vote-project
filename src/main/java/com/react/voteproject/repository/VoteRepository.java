package com.react.voteproject.repository;

import com.react.voteproject.entity.Vote;
import com.react.voteproject.repository.querydsl.VoteRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VoteRepository extends JpaRepository<Vote,Long>, VoteRepositoryCustom {


    @Query("select v from Vote v")
    Slice<Vote> findAllByVote(Pageable pageable);

    @Query("SELECT v, (SELECT SUM(vo.count) FROM VoteOption vo WHERE vo.vote.id = v.id) AS totalCount "
            + "FROM Vote v "
            + "ORDER BY totalCount desc")
    Slice<Object[]> findVoteWithTotalCount(Pageable pageable);
}
