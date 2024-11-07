package com.react.voteproject.repository;

import com.react.voteproject.entity.Bookmark;
import com.react.voteproject.entity.Up;
import com.react.voteproject.entity.User;
import com.react.voteproject.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {


    Optional<Bookmark> findByVoteAndUser(Vote vote , User user);

    Page<Bookmark> findByuser(PageRequest pageRequest, User user);
}
