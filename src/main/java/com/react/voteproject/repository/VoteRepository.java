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

    // 투표 참여 카테고리별 리스트 조회
    @Query("WITH comment_count " +
            "AS (" +
            "select v.id as vote_id,count(c.vote.id) as c_count " +
            "FROM Vote v " +
            "Left join Comment c ON v.id = c.vote.id " +
            "group by v.id) " +
            "select v,cc.c_count from Vote v " +
            "JOIN Fetch comment_count cc ON v.id = cc.vote_id " +
            "WHERE category = :category AND (:title IS NULL OR LOWER(v.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Slice<Object[]> findAllByVoteAndCategory(Pageable pageable, @Param("category") String category,@Param("title") String title);

    // 투표 참여 전체 리스트 조회
    @Query("WITH comment_count " +
            "AS (" +
            "select v.id as vote_id,count(c.vote.id) as c_count " +
            "FROM Vote v " +
            "Left join Comment c ON v.id = c.vote.id " +
            "group by v.id) " +
            "select v,cc.c_count from Vote v " +
            "JOIN Fetch comment_count cc ON v.id = cc.vote_id " +
            "WHERE (:title IS NULL OR LOWER(v.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Slice<Object[]> findAllByVote(Pageable pageable,@Param("title") String title);

    // 카테고리별 투표 참여율 내림차순 기준 리스트 조회
    @Query("WITH comment_count " +
            "AS (" +
            "select v.id as vote_id,count(c.vote.id) as c_count " +
            "FROM Vote v " +
            "Left join Comment c ON v.id = c.vote.id " +
            "group by v.id), " +
            "option_count as (" +
            "Select v.id as vote_id, SUM(vo.count) AS totalCount " +
            "FROM Vote v " +
            "Left JOIN VoteOption vo on vo.vote.id = v.id " +
            "GROUP BY v.id) " +
            "SELECT v,cc.c_count, oc.totalCount " +
            "FROM Vote v " +
            "JOIN fetch option_count oc on oc.vote_id = v.id " +
            "JOIN fetch comment_count cc on cc.vote_id = v.id " +
            "WHERE v.category = :category " +
            "ORDER BY oc.totalCount desc")
    Slice<Object[]> findVotesWithTotalCountByCategory(Pageable pageable,@Param("category") String category);

    // 전체 투표 참여율 내림차순 기준 리스트 조회
    @Query("WITH comment_count " +
            "AS (" +
            "select v.id as vote_id,count(c.vote.id) as c_count " +
            "FROM Vote v " +
            "Left join Comment c ON v.id = c.vote.id " +
            "group by v.id), " +
            "option_count as (" +
            "Select v.id as vote_id, SUM(vo.count) AS totalCount " +
            "FROM Vote v " +
            "Left JOIN VoteOption vo on vo.vote.id = v.id " +
            "GROUP BY v.id) " +
            "SELECT v,cc.c_count, oc.totalCount " +
            "FROM Vote v " +
            "JOIN fetch option_count oc on oc.vote_id = v.id " +
            "JOIN fetch comment_count cc on cc.vote_id = v.id " +
            "ORDER BY oc.totalCount desc")
    Slice<Object[]> findVoteWithTotalCount(Pageable pageable);

    // 총 투표 수 , 인기 카테고리 조회
    @Query("Select v.category," +
            "COUNT(v.category) AS HotCategory," +
            "(Select count(*) from Vote) AS vote_count " +
            "FROM User u " +
            "LEFT JOIN Vote v ON u.id = v.user.id " +
            "GROUP BY v.category " +
            "ORDER BY HotCategory DESC " +
            "LIMIT 1" )
    Object[] findHotCategoryWithVoteCount();

    // 인기 투표 조회
    @Query("WITH hot_vote AS (" +
            "select v.id AS vote_id, SUM(vo.count) AS user_count " +
            "from Vote v " +
            "Left JOIN VoteOption vo ON v.id = vo.vote.id AND YEAR(vo.createdDate) = YEAR(CURDATE()) AND MONTH(vo.createdDate) = MONTH(CURDATE()) " +
            "GROUP BY v.id ) " +
            "Select v, u, row_number() OVER (ORDER BY user_count DESC) AS vote_rank " +
            "from Vote v " +
            "JOIN fetch hot_vote hv ON v.id = hv.vote_id " +
            "JOIN fetch User u ON v.user.id = u.id ")
    List<Object[]> findHotVote(Pageable pageable);

    // 투표 추천 리스트 조회
    @Query("WITH hot_vote AS (" +
            "SELECT v.id AS vote_id, SUM(vo.count) AS user_count " +
            "FROM Vote v " +
            "LEFT JOIN VoteOption vo ON v.id = vo.vote.id " +
            "AND YEAR(vo.createdDate) = YEAR(CURDATE()) " +
            "AND MONTH(vo.createdDate) = MONTH(CURDATE()) " +
            "GROUP BY v.id)," +
            "comment_count as (" +
            "select v.id as vote_id, count(c.vote.id) as c_count , hv.user_count as user_count " +
            "from Vote v " +
            "Left Join hot_vote hv on v.id = hv.vote_id " +
            "Left join Comment c on v.id = c.vote.id " +
            "Group by v.id ) " +
            "SELECT v, u, cc.c_count, " +
            "ROW_NUMBER() OVER (ORDER BY cc.user_count DESC) AS vote_rank " +
            "FROM Vote v " +
            "JOIN fetch comment_count cc ON v.id = cc.vote_id " +
            "JOIN fetch User u ON v.user.id = u.id " +
            "WHERE (:userId IS NULL OR v.id NOT IN (SELECT uv.vote.id FROM UserVote uv WHERE uv.user.id = :userId)) " +
            "AND (:userId IS NULL OR v.user.id != :userId) ")
    List<Object[]> findHotVotesExcludingUser(@Param("userId") Long userId,Pageable pageable);



}
