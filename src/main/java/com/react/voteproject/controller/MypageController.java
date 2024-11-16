package com.react.voteproject.controller;


import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.MypageVoteDto;
import com.react.voteproject.dto.VoteResponseDto;
import com.react.voteproject.service.MypageService;
import com.react.voteproject.utility.ResponseHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MypageController {

    private  final MypageService mypageService;

    @GetMapping("/votes/mypage")
    public ResponseEntity<MypageVoteDto> findMyVotes(@PageableDefault(page = 1) Pageable pageable) {

        if(!AuthContext.checkAuth())
        {
            return ResponseEntity.status(HttpStatus.OK).body(new MypageVoteDto());
        }
        MypageVoteDto mypageVoteDto = mypageService.findVotes(pageable,AuthContext.getAuth());

        return ResponseEntity.status(HttpStatus.OK).body(mypageVoteDto);
    }

    @GetMapping("/ups/mypage")
    public ResponseEntity<MypageVoteDto> findMyUps(@PageableDefault(page = 1) Pageable pageable) {

        if(!AuthContext.checkAuth())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MypageVoteDto());
        }
        MypageVoteDto mypageVoteDto = mypageService.findups(pageable,AuthContext.getAuth());

        return ResponseEntity.status(HttpStatus.OK).body(mypageVoteDto);
    }

    @GetMapping("/uservotes/mypage")
    public ResponseEntity<MypageVoteDto> findMyUserVotes(@PageableDefault(page = 1) Pageable pageable) {

        if(!AuthContext.checkAuth())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MypageVoteDto());
        }
        MypageVoteDto mypageVoteDto = mypageService.finduserVotes(pageable,AuthContext.getAuth());

        return ResponseEntity.status(HttpStatus.OK).body(mypageVoteDto);
    }

    @GetMapping("/bookmarks/mypage")
    public ResponseEntity<MypageVoteDto> findMyBookmarks(@PageableDefault(page = 1) Pageable pageable) {

        if(!AuthContext.checkAuth())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MypageVoteDto());
        }
        MypageVoteDto mypageVoteDto = mypageService.findbookMarks(pageable,AuthContext.getAuth());

        return ResponseEntity.status(HttpStatus.OK).body(mypageVoteDto);
    }



}
