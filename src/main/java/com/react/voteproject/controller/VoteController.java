package com.react.voteproject.controller;


import com.react.voteproject.category.category_enum;
import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.*;
import com.react.voteproject.entity.Vote;
import com.react.voteproject.service.CommentService;
import com.react.voteproject.service.VoteService;
import com.react.voteproject.utility.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @GetMapping("/votes")
    public ResponseEntity<VoteDetailDataDto> findId(@RequestParam("id") Long vote_id) {
        VoteDetailDataDto vote = voteService.findvotesId(vote_id);
        return ResponseEntity.status(HttpStatus.OK).body(vote);
    }

    @GetMapping("/votes/summary")
    public ResponseEntity<HotCategoryAndTotalDto> getSummary() {
        HotCategoryAndTotalDto hotCategoryAndTotalDto = voteService.getSummary();
        return ResponseEntity.status(HttpStatus.OK).body(hotCategoryAndTotalDto);
    }

    @GetMapping("/votes/hot")
    public ResponseEntity<HotVoteandRankDto> getHot() {
        HotVoteandRankDto hotVoteandRankDto = voteService.getHot();
        return ResponseEntity.status(HttpStatus.OK).body(hotVoteandRankDto);
    }

    @GetMapping("/votes/recommend")
    public ResponseEntity<HotVoteandRankDto> getRecommend() {
        HotVoteandRankDto hotVoteandRankDto = voteService.getRecommend();
        return ResponseEntity.status(HttpStatus.OK).body(hotVoteandRankDto);
    }

    @GetMapping("/votes/all")
    public ResponseEntity<VoteHomeDataDto> findAll(@PageableDefault(page = 1) Pageable pageable, @RequestParam(value = "sort",defaultValue = "startDay")  String sort, @RequestParam(value = "category",defaultValue = "")  String category,@RequestParam(value = "title",required = false) String title) {
        if(title != null) {
            if(title.length() == 0 || title.length() > 20) {
                return ResponseEntity.status(HttpStatus.OK).body(VoteHomeDataDto.createVoteHomeDataDto(new ArrayList<>(),false,0));
            }
        }
        VoteHomeDataDto vote = voteService.findAll(pageable,sort,category,title);

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

    @PostMapping("/ups")
    public ResponseEntity<Map<Object,Object>> changeUp(@RequestParam("id") Long id) {

        if(!AuthContext.checkAuth())
        {
            return ResponseHelper.createErrorMessage("result","로그인을 해주세요");
        }
        Boolean status = voteService.changeUp(id);
        if(!status)
        {
            return ResponseHelper.createErrorMessage("result","좋아요 실패");
        }
        return ResponseHelper.createSuccessMessage("result","좋아요 성공");
    }

    @PostMapping("/bookmarks")
    public ResponseEntity<Map<Object,Object>> changeBookmark(@RequestParam("id") Long id) {

        if(!AuthContext.checkAuth())
        {
            return ResponseHelper.createErrorMessage("result","로그인을 해주세요");
        }
        Boolean status = voteService.changeBookmark(id);
        if(!status)
        {
            return ResponseHelper.createErrorMessage("result","북마크 실패");
        }
        return ResponseHelper.createSuccessMessage("result","북마크 성공");
    }


}
