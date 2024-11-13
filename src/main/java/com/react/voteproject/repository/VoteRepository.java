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


    @Query("select v from Vote v WHERE category = :category AND (:title IS NULL OR LOWER(v.title) LIKE LOWER(CONCAT('%', :title, '%'))) ")
    Slice<Vote> findAllByVoteAndCategory(Pageable pageable, @Param("category") String category,@Param("title") String title);

    @Query("select v from Vote v WHERE (:title IS NULL OR LOWER(v.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Slice<Vote> findAllByVote(Pageable pageable,@Param("title") String title);

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

    @Query("Select v.category," +
            "COUNT(v.category) AS HotCategory," +
            "(Select count(*) from Vote) AS vote_count " +
            "FROM User u " +
            "LEFT JOIN Vote v ON u.id = v.user.id " +
            "GROUP BY v.category " +
            "ORDER BY HotCategory DESC " +
            "LIMIT 1" )
    Object[] findHotCategoryWithVoteCount();

    @Query(value = "WITH hot_vote AS (select v.id AS vote_id, SUM(vo.count) AS user_count " +
            "from Vote v " +
            "LEFT JOIN VoteOption vo ON v.id = vo.vote.id AND YEAR(vo.createdDate) = YEAR(CURDATE()) AND MONTH(vo.createdDate) = MONTH(CURDATE()) " +
            "GROUP BY v.id ) " +
            "Select v, row_number() OVER (ORDER BY user_count DESC) AS vote_rank " +
            "from Vote v " +
            "LEFT JOIN hot_vote hv ON v.id = hv.vote_id ")
    List<Object[]> findHotVote(Pageable pageable);



}
