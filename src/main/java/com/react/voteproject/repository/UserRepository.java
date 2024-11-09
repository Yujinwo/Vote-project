package com.react.voteproject.repository;


import com.react.voteproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u Where u.userId = :user_id and u.userPw = :user_pw")
    Optional<User> Login(@Param("user_id") String user_id, @Param("user_pw") String user_pw);

    Optional<User> findByuserId(String user_id);

    @Query("select u.userId,u.userPw,COUNT(distinct v.id) AS vote_count, COUNT(distinct p.id) AS up_count, COUNT(distinct c.id) AS comment_count " +
            "from User u " +
            "LEFT JOIN Vote v ON u.id = v.user.id " +
            "LEFT JOIN Up p ON u.id = p.user.id " +
            "LEFT JOIN Comment c ON u.id = c.user.id " +
            "where u.id = :user_id ")
    Optional<Object[]> findStatsByUser(@Param("user_id") Long user_id);


    @Query("select ( COUNT(uv.user.id) / (select count(*) from Vote v) ) * 100 AS participation_rate from User u " +
            "LEFT JOIN UserVote uv ON u.id = uv.user.id " +
            "Where u.id = :user_id ")
    Double findVotingRateByUser(@Param("user_id") Long user_id);


    @Query(value = "WITH UserVoteCounts AS (" +
            "    SELECT " +
            "        u.id AS user_id, " +
            "        v.category, " +
            "        COUNT(v.id) AS vote_count " +
            "    FROM " +
            "        user u " +
            "    LEFT JOIN " +
            "        vote v ON u.id = v.user_id " +
            "    WHERE " +
            "        v.created_date >= CURDATE() AND v.created_date < CURDATE() + INTERVAL 1 DAY " +
            "    GROUP BY " +
            "        u.id, v.category" +
            "), " +
            "UserActivity AS (" +
            "    SELECT " +
            "        u.id AS user_id, " +
            "        COUNT(DISTINCT v.id) AS vote_count, " +
            "        COUNT(DISTINCT p.id) AS up_count, " +
            "        COUNT(DISTINCT c.id) AS comment_count, " +
            "        COUNT(DISTINCT v.id) + COUNT(DISTINCT p.id) + COUNT(DISTINCT c.id) AS total_activity " +
            "    FROM " +
            "        user u " +
            "    LEFT JOIN " +
            "        vote v ON u.id = v.user_id AND v.created_date >= CURDATE() AND v.created_date < CURDATE() + INTERVAL 1 DAY " +
            "    LEFT JOIN " +
            "        up p ON u.id = p.user_id AND p.created_date >= CURDATE() AND p.created_date < CURDATE() + INTERVAL 1 DAY " +
            "    LEFT JOIN " +
            "        comment c ON u.id = c.user_id AND c.created_date >= CURDATE() AND c.created_date < CURDATE() + INTERVAL 1 DAY " +
            "    GROUP BY " +
            "        u.id" +
            "), " +
            "UserVoteRate AS (" +
                "SELECT " +
                    "u.id AS user_id, " +
                    "( COUNT(uv.id) / (select count(*) from Vote v where v.created_date >= CURDATE() AND v.created_date < CURDATE() + INTERVAL 1 DAY ) ) * 100 AS participation_rate from User u " +
                    "LEFT JOIN User_Vote uv ON u.id = uv.user_id " +
                    "where uv.created_date >= CURDATE() AND uv.created_date < CURDATE() + INTERVAL 1 DAY " +
                    "GROUP BY u.id " +
            "), " +
            "RankedCategories AS (" +
            "    SELECT " +
            "        user_id, " +
            "        category, " +
            "        vote_count, " +
            "        ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY vote_count DESC) AS category_rank " +
            "    FROM " +
            "        UserVoteCounts" +
            ") " +
            "SELECT " +
            "    u.user_id AS user_id, " +
            "    u.user_nick AS user_nick, " +
            "    ua.vote_count, " +
            "    ua.up_count, " +
            "    ua.comment_count, " +
            "    ua.total_activity, " +
            "    FLOOR(uvr.participation_rate), " +
            "    rc.category AS main_category, " +
            "    ROW_NUMBER() OVER (ORDER BY ua.total_activity DESC) AS overall_rank " +

            "FROM " +
            "    User u " +
            "LEFT JOIN " +
            "    RankedCategories rc ON u.id = rc.user_id AND rc.category_rank = 1 " +
            "LEFT JOIN " +
            "    UserActivity ua ON u.id = ua.user_id " +
            "LEFT JOIN " +
            "    UserVoteRate uvr ON u.id = uvr.user_id " +
            "ORDER BY " +
            "    ua.total_activity DESC limit 100"  , nativeQuery = true)
    List<Object[]> findUserRankingWithActivityToday();



    @Query(value = "WITH UserVoteCounts AS (" +
            "    SELECT " +
            "        u.id AS user_id, " +
            "        v.category, " +
            "        COUNT(v.id) AS vote_count " +
            "    FROM " +
            "        user u " +
            "    LEFT JOIN " +
            "        vote v ON u.id = v.user_id " +
            "    WHERE " +
            "        YEAR(v.created_date) = YEAR(CURDATE()) AND MONTH(v.created_date) = MONTH(CURDATE()) " +
            "    GROUP BY " +
            "        u.id, v.category" +
            "), " +
            "UserActivity AS (" +
            "    SELECT " +
            "        u.id AS user_id, " +
            "        COUNT(DISTINCT v.id) AS vote_count, " +
            "        COUNT(DISTINCT p.id) AS up_count, " +
            "        COUNT(DISTINCT c.id) AS comment_count, " +
            "        COUNT(DISTINCT v.id) + COUNT(DISTINCT p.id) + COUNT(DISTINCT c.id) AS total_activity " +
            "    FROM " +
            "        user u " +
            "    LEFT JOIN " +
            "        vote v ON u.id = v.user_id AND YEAR(v.created_date) = YEAR(CURDATE()) AND MONTH(v.created_date) = MONTH(CURDATE()) " +
            "    LEFT JOIN " +
            "        up p ON u.id = p.user_id AND YEAR(p.created_date) = YEAR(CURDATE()) AND MONTH(p.created_date) = MONTH(CURDATE()) " +
            "    LEFT JOIN " +
            "        comment c ON u.id = c.user_id AND YEAR(c.created_date) = YEAR(CURDATE()) AND MONTH(c.created_date) = MONTH(CURDATE()) " +
            "    GROUP BY " +
            "        u.id" +
            "), " +
            "UserVoteRate AS (" +
            "SELECT " +
            "u.id AS user_id, " +
            "( COUNT(uv.id) / (select count(*) from Vote v where YEAR(v.created_date) = YEAR(CURDATE()) AND MONTH(v.created_date) = MONTH(CURDATE()) ) ) * 100 AS participation_rate from User u " +
            "LEFT JOIN User_Vote uv ON u.id = uv.user_id " +
            "where YEAR(uv.created_date) = YEAR(CURDATE()) AND MONTH(uv.created_date) = MONTH(CURDATE()) " +
            "GROUP BY u.id " +
            "), " +
            "RankedCategories AS (" +
            "    SELECT " +
            "        user_id, " +
            "        category, " +
            "        vote_count, " +
            "        ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY vote_count DESC) AS category_rank " +
            "    FROM " +
            "        UserVoteCounts" +
            ") " +
            "SELECT " +
            "    u.user_id AS user_id, " +
            "    u.user_nick AS user_nick, " +
            "    ua.vote_count, " +
            "    ua.up_count, " +
            "    ua.comment_count, " +
            "    ua.total_activity, " +
            "    FLOOR(uvr.participation_rate), " +
            "    rc.category AS main_category, " +
            "    ROW_NUMBER() OVER (ORDER BY ua.total_activity DESC) AS overall_rank " +

            "FROM " +
            "    User u " +
            "LEFT JOIN " +
            "    RankedCategories rc ON u.id = rc.user_id AND rc.category_rank = 1 " +
            "LEFT JOIN " +
            "    UserActivity ua ON u.id = ua.user_id " +
            "LEFT JOIN " +
            "    UserVoteRate uvr ON u.id = uvr.user_id " +
            "ORDER BY " +
            "    ua.total_activity DESC limit 100"  , nativeQuery = true)
    List<Object[]> findUserRankingWithActivityThisMonth();


    @Query(value = "WITH UserVoteCounts AS (" +
            "    SELECT " +
            "        u.id AS user_id, " +
            "        v.category, " +
            "        COUNT(v.id) AS vote_count " +
            "    FROM " +
            "        user u " +
            "    LEFT JOIN " +
            "        vote v ON u.id = v.user_id " +
            "    WHERE " +
            "        YEAR(v.created_date) = YEAR(CURDATE()) " +
            "    GROUP BY " +
            "        u.id, v.category" +
            "), " +
            "UserActivity AS (" +
            "    SELECT " +
            "        u.id AS user_id, " +
            "        COUNT(DISTINCT v.id) AS vote_count, " +
            "        COUNT(DISTINCT p.id) AS up_count, " +
            "        COUNT(DISTINCT c.id) AS comment_count, " +
            "        COUNT(DISTINCT v.id) + COUNT(DISTINCT p.id) + COUNT(DISTINCT c.id) AS total_activity " +
            "    FROM " +
            "        user u " +
            "    LEFT JOIN " +
            "        vote v ON u.id = v.user_id AND YEAR(v.created_date) = YEAR(CURDATE()) " +
            "    LEFT JOIN " +
            "        up p ON u.id = p.user_id AND YEAR(p.created_date) = YEAR(CURDATE()) " +
            "    LEFT JOIN " +
            "        comment c ON u.id = c.user_id AND YEAR(c.created_date) = YEAR(CURDATE()) " +
            "    GROUP BY " +
            "        u.id" +
            "), " +
            "UserVoteRate AS (" +
            "SELECT " +
            "u.id AS user_id, " +
            "( COUNT(uv.id) / (select count(*) from Vote v where YEAR(v.created_date) = YEAR(CURDATE())) ) * 100 AS participation_rate from User u " +
            "LEFT JOIN user_vote uv ON u.id = uv.user_id " +
            "where YEAR(uv.created_date) = YEAR(CURDATE()) " +
            "GROUP BY u.id " +
            "), " +
            "RankedCategories AS (" +
            "    SELECT " +
            "        user_id, " +
            "        category, " +
            "        vote_count, " +
            "        ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY vote_count DESC) AS category_rank " +
            "    FROM " +
            "        UserVoteCounts" +
            ") " +
            "SELECT " +
            "    u.user_id AS user_id, " +
            "    u.user_nick AS user_nick, " +
            "    ua.vote_count, " +
            "    ua.up_count, " +
            "    ua.comment_count, " +
            "    ua.total_activity, " +
            "    FLOOR(uvr.participation_rate), " +
            "    rc.category AS main_category, " +
            "    ROW_NUMBER() OVER (ORDER BY ua.total_activity DESC) AS overall_rank " +

            "FROM " +
            "    User u " +
            "LEFT JOIN " +
            "    RankedCategories rc ON u.id = rc.user_id AND rc.category_rank = 1 " +
            "LEFT JOIN " +
            "    UserActivity ua ON u.id = ua.user_id " +
            "LEFT JOIN " +
            "    UserVoteRate uvr ON u.id = uvr.user_id " +
            "ORDER BY " +
            "    ua.total_activity DESC limit 100"  , nativeQuery = true)
    List<Object[]> findUserRankingWithActivityThisYear();




    @Query(value = "WITH UserActivity AS (" +
            "    SELECT " +
            "        u.id AS user_id, " +
            "        COUNT(DISTINCT v.id) AS vote_count, " +
            "        COUNT(DISTINCT p.id) AS up_count, " +
            "        COUNT(DISTINCT c.id) AS comment_count, " +
            "        COUNT(DISTINCT v.id) + COUNT(DISTINCT p.id) + COUNT(DISTINCT c.id) AS total_activity " +
            "    FROM " +
            "        user u " +
            "    LEFT JOIN " +
            "        vote v ON u.id = v.user_id AND v.created_date >= CURDATE() AND v.created_date < CURDATE() + INTERVAL 1 DAY AND v.category = :category" +
            "    LEFT JOIN " +
            "        up p ON u.id = p.user_id AND p.created_date >= CURDATE() AND p.created_date < CURDATE() + INTERVAL 1 DAY AND p.vote_id IN (" +
            "              SELECT v2.id FROM vote v2 " +
            "              WHERE v2.user_id = u.id " +
            "              AND v2.category = :category" +
            "          ) " +
            "    LEFT JOIN " +
            "        comment c ON u.id = c.user_id AND c.created_date >= CURDATE() AND c.created_date < CURDATE() + INTERVAL 1 DAY AND c.vote_id IN (" +
            "              SELECT v3.id FROM vote v3 " +
            "              WHERE v3.user_id = u.id " +
            "              AND v3.category = :category" +
            "          ) " +
            "    WHERE v.category = :category " +
            "    GROUP BY " +
            "        u.id" +
            "), " +
            "UserVoteRate AS (" +
            "SELECT " +
            "u.id AS user_id, " +
            "( COUNT(uv.id) / (select count(*) from Vote v where v.created_date >= CURDATE() AND v.created_date < CURDATE() + INTERVAL 1 DAY AND v.category = :category )) * 100 AS participation_rate from User u " +
            "LEFT JOIN User_Vote uv ON u.id = uv.user_id " +
            "AND uv.vote_id IN (" +
            "  SELECT v.id FROM vote v " +
            "  WHERE v.user_id = u.id " +
            "  AND v.category = :category " +
            ") " +
            "where uv.created_date >= CURDATE() AND uv.created_date < CURDATE() + INTERVAL 1 DAY " +
            "GROUP BY u.id " +
            ") " +
            "SELECT " +
            "    u.user_id AS user_id, " +
            "    u.user_nick AS user_nick, " +
            "    ua.vote_count, " +
            "    ua.up_count, " +
            "    ua.comment_count, " +
            "    ua.total_activity, " +
            "    FLOOR(uvr.participation_rate), " +
            "    ROW_NUMBER() OVER (ORDER BY ua.total_activity DESC) AS overall_rank " +
            "FROM " +
            "    user u " +
            "LEFT JOIN " +
            "    UserActivity ua ON u.id = ua.user_id " +
            "LEFT JOIN " +
            "    UserVoteRate uvr ON u.id = uvr.user_id " +
            "ORDER BY " +
            "    ua.total_activity DESC limit 100"  , nativeQuery = true)
    List<Object[]> findUserCategoryRankingWithActivityToday(@Param("category") String category);


    @Query(value = "WITH UserActivity AS (" +
            "    SELECT " +
            "        u.id AS user_id, " +
            "        COUNT(DISTINCT v.id) AS vote_count, " +
            "        COUNT(DISTINCT p.id) AS up_count, " +
            "        COUNT(DISTINCT c.id) AS comment_count, " +
            "        COUNT(DISTINCT v.id) + COUNT(DISTINCT p.id) + COUNT(DISTINCT c.id) AS total_activity " +
            "    FROM " +
            "        user u " +
            "    LEFT JOIN " +
            "        vote v ON u.id = v.user_id AND YEAR(v.created_date) = YEAR(CURDATE()) AND MONTH(v.created_date) = MONTH(CURDATE()) AND v.category = :category" +
            "    LEFT JOIN " +
            "        up p ON u.id = p.user_id AND YEAR(p.created_date) = YEAR(CURDATE()) AND MONTH(p.created_date) = MONTH(CURDATE()) AND p.vote_id IN (" +
            "              SELECT v2.id FROM vote v2 " +
            "              WHERE v2.user_id = u.id " +
            "              AND v2.category = :category" +
            "          ) " +
            "    LEFT JOIN " +
            "        comment c ON u.id = c.user_id AND YEAR(c.created_date) = YEAR(CURDATE()) AND MONTH(c.created_date) = MONTH(CURDATE()) AND c.vote_id IN (" +
            "              SELECT v3.id FROM vote v3 " +
            "              WHERE v3.user_id = u.id " +
            "              AND v3.category = :category" +
            "          ) " +
            "    WHERE v.category = :category " +
            "    GROUP BY " +
            "        u.id" +
            "), " +
            "UserVoteRate AS (" +
            "SELECT " +
            "u.id AS user_id, " +
            "( COUNT(uv.id) / (select count(*) from Vote v where YEAR(v.created_date) = YEAR(CURDATE()) AND MONTH(v.created_date) = MONTH(CURDATE()) AND v.category = :category )) * 100 AS participation_rate from User u " +
            "LEFT JOIN User_Vote uv ON u.id = uv.user_id " +
            "AND uv.vote_id IN (" +
            "  SELECT v.id FROM vote v " +
            "  WHERE v.user_id = u.id " +
            "  AND v.category = :category " +
            ") " +
            "where YEAR(uv.created_date) = YEAR(CURDATE()) AND MONTH(uv.created_date) = MONTH(CURDATE())" +
            "GROUP BY u.id " +
            ") " +
            "SELECT " +
            "    u.user_id AS user_id, " +
            "    u.user_nick AS user_nick, " +
            "    ua.vote_count, " +
            "    ua.up_count, " +
            "    ua.comment_count, " +
            "    ua.total_activity, " +
            "    FLOOR(uvr.participation_rate), " +
            "    ROW_NUMBER() OVER (ORDER BY ua.total_activity DESC) AS overall_rank " +
            "FROM " +
            "    user u " +
            "LEFT JOIN " +
            "    UserActivity ua ON u.id = ua.user_id " +
            "LEFT JOIN " +
            "    UserVoteRate uvr ON u.id = uvr.user_id " +
            "ORDER BY " +
            "    ua.total_activity DESC limit 100"  , nativeQuery = true)
    List<Object[]> findUserCategoryRankingWithActivityThisMonth(@Param("category") String category);

    @Query(value = "WITH UserActivity AS (" +
            "    SELECT " +
            "        u.id AS user_id, " +
            "        COUNT(DISTINCT v.id) AS vote_count, " +
            "        COUNT(DISTINCT p.id) AS up_count, " +
            "        COUNT(DISTINCT c.id) AS comment_count, " +
            "        COUNT(DISTINCT v.id) + COUNT(DISTINCT p.id) + COUNT(DISTINCT c.id) AS total_activity " +
            "    FROM " +
            "        user u " +
            "    LEFT JOIN " +
            "        vote v ON u.id = v.user_id AND YEAR(v.created_date) = YEAR(CURDATE()) AND v.category = :category" +
            "    LEFT JOIN " +
            "        up p ON u.id = p.user_id AND YEAR(p.created_date) = YEAR(CURDATE()) AND p.vote_id IN (" +
            "              SELECT v2.id FROM vote v2 " +
            "              WHERE v2.user_id = u.id " +
            "              AND v2.category = :category" +
            "          ) " +
            "    LEFT JOIN " +
            "        comment c ON u.id = c.user_id AND YEAR(c.created_date) = YEAR(CURDATE()) AND c.vote_id IN (" +
            "              SELECT v3.id FROM vote v3 " +
            "              WHERE v3.user_id = u.id " +
            "              AND v3.category = :category" +
            "          ) " +
            "    WHERE v.category = :category " +
            "    GROUP BY " +
            "        u.id" +
            "), " +
            "UserVoteRate AS (" +
            "SELECT " +
            "u.id AS user_id, " +
            "( COUNT(uv.id) / (select count(*) from Vote v where YEAR(v.created_date) = YEAR(CURDATE()) AND v.category = :category )) * 100 AS participation_rate from User u " +
            "LEFT JOIN User_Vote uv ON u.id = uv.user_id " +
            "AND uv.vote_id IN (" +
            "  SELECT v.id FROM vote v " +
            "  WHERE v.user_id = u.id " +
            "  AND v.category = :category " +
            ") " +
            "where YEAR(uv.created_date) = YEAR(CURDATE())" +
            "GROUP BY u.id " +
            ") " +
            "SELECT " +
            "    u.user_id AS user_id, " +
            "    u.user_nick AS user_nick, " +
            "    ua.vote_count, " +
            "    ua.up_count, " +
            "    ua.comment_count, " +
            "    ua.total_activity, " +
            "   FLOOR(uvr.participation_rate), " +
            "    ROW_NUMBER() OVER (ORDER BY ua.total_activity DESC) AS overall_rank " +
            "FROM " +
            "    user u " +
            "LEFT JOIN " +
            "    UserActivity ua ON u.id = ua.user_id " +
            "LEFT JOIN " +
            "    UserVoteRate uvr ON u.id = uvr.user_id " +
            "ORDER BY " +
            "    ua.total_activity DESC limit 100"  , nativeQuery = true)
    List<Object[]> findUserCategoryRankingWithActivityThisYear(@Param("category") String category);

}
