package com.react.voteproject.controller;


import com.react.voteproject.category.category_enum;
import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.CommentWriteDto;
import com.react.voteproject.dto.VoteDetailDataDto;
import com.react.voteproject.dto.VoteWriteDto;
import com.react.voteproject.entity.Vote;
import com.react.voteproject.service.CommentService;
import com.react.voteproject.service.VoteService;
import com.react.voteproject.utility.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final CommentService commentService;


    @GetMapping("/votes")
    public ResponseEntity<VoteDetailDataDto> findVote(@RequestParam("id") Long id) {
        VoteDetailDataDto vote = voteService.findvotes(id);

        return ResponseEntity.status(HttpStatus.OK).body(vote);
    }
    @PostMapping("/votes")
    public ResponseEntity<Map<String,Object>> writeVote(@Valid @RequestBody VoteWriteDto voteWriteDto) {

        Boolean checkCategory = category_enum.fromCode(voteWriteDto.getCategory());
        if(!checkCategory){
            return ResponseHelper.createErrorMessage("result","카테고리가 일치하지 않습니다");
        }
        if(!AuthContext.checkAuth())
        {
            return ResponseHelper.createErrorMessage("result","로그인을 해주세요");
        }
        Boolean status = voteService.write(voteWriteDto);
        if(!status)
        {
            return ResponseHelper.createErrorMessage("result","투표 작성 실패");
        }
        return ResponseHelper.createSuccessMessage("result","투표 작성 성공");
    }
    @PostMapping("/voteoptions")
    public ResponseEntity<Map<String,Object>> changeVoteOption(@RequestParam("id") Long id) {

        if(!AuthContext.checkAuth())
        {
            return ResponseHelper.createErrorMessage("result","로그인을 해주세요");
        }
        Boolean status = voteService.changeVoteOption(id);
        if(!status)
        {
            return ResponseHelper.createErrorMessage("result","선택 실패");
        }
        return ResponseHelper.createSuccessMessage("result","선택 성공");
    }

    @PostMapping("/comments")
    public ResponseEntity<Map<String,Object>> wrtieComment(@Valid @RequestBody CommentWriteDto commentWriteDto) {

        if(!AuthContext.checkAuth())
        {
            return ResponseHelper.createErrorMessage("result","로그인을 해주세요");
        }
        Boolean status = commentService.write(commentWriteDto);
        if(!status)
        {
            return ResponseHelper.createErrorMessage("result","댓글 작성 실패");
        }
        return ResponseHelper.createSuccessMessage("result","댓글 작성 성공");
    }

}
