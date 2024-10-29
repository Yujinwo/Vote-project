package com.react.voteproject.controller;


import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.CommentMoreDto;
import com.react.voteproject.dto.CommentWriteDto;
import com.react.voteproject.service.CommentService;
import com.react.voteproject.utility.ResponseHelper;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<CommentMoreDto> findComment(@RequestParam("id") Long id ,@PageableDefault(page = 1) Pageable pageable) {
        CommentMoreDto commentMoreDto=  commentService.findComment(id,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(commentMoreDto);
    }
    @PostMapping("/comments")
    public ResponseEntity<Map<Object,Object>> writeComment(@Valid @RequestBody CommentWriteDto commentWriteDto) {

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
