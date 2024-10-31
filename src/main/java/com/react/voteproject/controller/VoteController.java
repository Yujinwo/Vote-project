package com.react.voteproject.controller;


import com.react.voteproject.category.category_enum;
import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.CommentWriteDto;
import com.react.voteproject.dto.VoteDetailDataDto;
import com.react.voteproject.dto.VoteUpdateDto;
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



    @GetMapping("/votes")
    public ResponseEntity<VoteDetailDataDto> findVote(@RequestParam("id") Long vote_id) {
        VoteDetailDataDto vote = voteService.findvotes(vote_id);

        return ResponseEntity.status(HttpStatus.OK).body(vote);
    }
    @PostMapping("/votes")
    public ResponseEntity<Map<Object,Object>> writeVote(@Valid @RequestBody VoteWriteDto voteWriteDto) {

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

    @PutMapping("/votes")
    public ResponseEntity<Map<Object,Object>> updateVote(@Valid @RequestBody VoteUpdateDto voteUpdateDto) {

        Boolean checkCategory = category_enum.fromCode(voteUpdateDto.getCategory());
        if(!checkCategory){
            return ResponseHelper.createErrorMessage("result","카테고리가 일치하지 않습니다");
        }
        if(!AuthContext.checkAuth())
        {
            return ResponseHelper.createErrorMessage("result","로그인을 해주세요");
        }
        Boolean status = voteService.update(voteUpdateDto);
        if(!status)
        {
            return ResponseHelper.createErrorMessage("result","투표 수정 실패");
        }
        return ResponseHelper.createSuccessMessage("result","투표 수정 성공");
    }
    @DeleteMapping("/votes")
    public ResponseEntity<Map<Object,Object>> deleteVote(@RequestParam("id") Long vote_id) {

        if(!AuthContext.checkAuth())
        {
            return ResponseHelper.createErrorMessage("result","로그인을 해주세요");
        }
        Boolean status = voteService.delete(vote_id);
        if(!status)
        {
            return ResponseHelper.createErrorMessage("result","투표 삭제 실패");
        }
        return ResponseHelper.createSuccessMessage("result","투표 삭제 성공");
    }
    @PostMapping("/voteoptions")
    public ResponseEntity<Map<Object,Object>> changeVoteOption(@RequestParam("id") Long id) {

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



}
