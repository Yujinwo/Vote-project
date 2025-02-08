package com.react.voteproject.service;


import com.react.voteproject.category.category_enum;
import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.*;
import com.react.voteproject.entity.*;
import com.react.voteproject.exception.CreationException;
import com.react.voteproject.exception.UnauthorizedException;
import com.react.voteproject.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
     private final VoteRepository voteRepository;
     private final VoteOptionRepository voteOptionRepository;
     private final UserVoteRepository userVoteRepository;
     private final UpRepository upRepository;
     private final BookmarkRepository bookmarkRepository;
     private final CommentRepository commentRepository;
     private final EntityManager em;


     // 투표 작성
     @Transactional
     public void write(VoteWriteDto voteWriteDto) {
        Vote vote = Optional.of(voteRepository.save(voteWriteDto.createVote())).orElseThrow(() -> new CreationException("투표가 존재하지 않습니다."));
        em.flush();
        em.clear();
        // 투표 선택지 저장
        for (String option : voteWriteDto.getChoices())
        {
            VoteOption voteOption = Optional.of(voteOptionRepository.save(VoteOption.builder().content(option).vote(vote).build())).orElseThrow(() -> new CreationException("투표 선택지 저장을 실패했습니다. 잠시 후 다시 시도해 주세요!"));
        }
    }
    // 투표 수정
    @Transactional
    public void update(VoteUpdateDto voteUpdateDto) {
        Vote vote = voteRepository.findById(voteUpdateDto.getVote_id()).orElseThrow(() -> new CreationException("투표가 존재하지 않습니다."));
                // 투표 작성자와 일치하지 않다면
        if(!vote.getUser().getUserId().equals(AuthContext.getAuth().getUserId())) {
                    throw new UnauthorizedException("투표 작성자와 일치하지 않습니다.");
        }
        List<LocalDateTime> dateTimeList = voteUpdateDto.changeDayFormat();
        vote.changeVoteContent(voteUpdateDto.getTitle(), voteUpdateDto.getCategory(), dateTimeList.get(0),dateTimeList.get(1));
        List<VoteOption> voteOption = voteOptionRepository.findByvote(vote);
        for (int i = 0; i < voteOption.size(); i++) {
                        String option = voteOption.get(i).getContent();
                        if(!voteUpdateDto.getChoices().get(i).equals(option)) {
                            voteOption.get(i).changeContent(voteUpdateDto.getChoices().get(i));
                        }
        }
    }
    // 투표 상세 조회
    @Transactional
    public VoteDetailDataDto findvotesId(Long id) {
        Optional<Vote> vote = voteRepository.findById(id);
        if(vote.isPresent()) {
            // 이미 투표 선택했는지 조회
            Long userSelectedId = null;

            // 투표 댓글 Slice 조회
            PageRequest pageRequest = PageRequest.of(0,10);
            Slice<Comment> comments = commentRepository.findPageByVote(vote.get(), pageRequest);
            List<CommentResponseDto> commentList = comments.getContent().stream().map(CommentResponseDto::createCommentResponseDto).collect(Collectors.toList());

            // 투표 데이터 조회
            VoteResponseDto voteResponseDto = vote.map(v -> VoteResponseDto.createVoteResponseDto(v,commentRepository.findCommentcountByVote(vote.get()))).get();

            Boolean hasUp = false;
            Boolean hasBookmark = false;
            if(upRepository.findByVoteAndUser(vote.get(),AuthContext.getAuth()).isPresent()) {
                hasUp = true;
            }

            if(bookmarkRepository.findByVoteAndUser(vote.get(),AuthContext.getAuth()).isPresent()) {
                hasBookmark = true;
            }

            if(AuthContext.checkAuth()) {
                Optional<UserVote> userVote = userVoteRepository.findByVoteAndUser(vote.get(),AuthContext.getAuth());
                if(userVote.isPresent())
                {
                    userSelectedId = userVote.get().getVoteOption().getId();
                    return VoteDetailDataDto.createVoteDetailDataDto(voteResponseDto,userSelectedId,commentList,comments.hasNext(),hasUp,hasBookmark);
                }
                else {
                    return VoteDetailDataDto.createVoteDetailDataDto(voteResponseDto,userSelectedId,commentList,comments.hasNext(), hasUp, hasBookmark);
                }
            }
            else {
                return VoteDetailDataDto.createVoteDetailDataDto(voteResponseDto,userSelectedId,commentList,comments.hasNext(), hasUp, hasBookmark);
            }
        }
        else {
            return new VoteDetailDataDto();
        }

    }
    // 유저 투표 참여 선택지 변경
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void changeVoteOption(Long id) {
        VoteOption voteOption = voteOptionRepository.findById(id).orElseThrow(() -> new CreationException("투표 선택지가 존재하지 않습니다."));
        // 기존 선택지 삭제 -> 변경한 선택지로 수정
        Optional<UserVote> userVote = userVoteRepository.findByVoteAndUser(voteOption.getVote(),AuthContext.getAuth());
        if(userVote.isPresent())
        {
                userVoteRepository.delete(userVote.get());
                userVote.get().getVoteOption().downCount();
                voteOption.upCount();
        }
        else {
                voteOption.upCount();
        }
        try {
                Optional<UserVote> save = Optional.of(userVoteRepository.save(voteOption.createUserVote()));
        }
        catch (DataIntegrityViolationException e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new CreationException("중복 투표는 허용되지 않습니다");
        }
    }

    // 투표 삭제
    @Transactional
    public void delete(Long voteId) {
        Optional<Vote> vote = voteRepository.findById(voteId);
        if(vote.isPresent())
        {
            // 투표 작성자와 일치하지 않다면
            if(!vote.get().getUser().getUserId().equals(AuthContext.getAuth().getUserId())) {
                throw new UnauthorizedException("투표 작성자와 일치하지 않습니다.");
            }
            // 투표 삭제
            voteRepository.delete(vote.get());
        }
    }
    // 좋아요
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void changeUp(Long voteId) {
         Vote vote = voteRepository.findById(voteId).orElseThrow(() -> new CreationException("투표가 존재하지 않습니다."));

         Optional<Up> up = upRepository.findByVoteAndUser(vote,AuthContext.getAuth());
         if(up.isPresent()) {
                // 좋아요 -1
                upRepository.delete(up.get());
                vote.changeUp(-1);
         }
         else {
                // 좋아요 +1
                Up saveUp = Up.builder().user(AuthContext.getAuth()).vote(vote).build();
                Up savedUp = Optional.of(upRepository.save(saveUp)).orElseThrow(() -> new CreationException("좋아요 실패했습니다. 잠시 후 다시 시도해 주세요!"));
                vote.changeUp(1);
         }
    }
    // 북마크
    @Transactional
    public void changeBookmark(Long voteId) {
        Vote vote = voteRepository.findById(voteId).orElseThrow(() -> new CreationException("투표가 존재하지 않습니다."));

        Optional<Bookmark> bookmark = bookmarkRepository.findByVoteAndUser(vote,AuthContext.getAuth());
        if(bookmark.isPresent()) {
                // 북마크 해제
                bookmarkRepository.delete(bookmark.get());
        }
        else {
                // 북마크 등록
                Bookmark saveBookmark = Bookmark.builder().user(AuthContext.getAuth()).vote(vote).build();
                Bookmark savedBookmark = Optional.of(bookmarkRepository.save(saveBookmark)).orElseThrow(() -> new CreationException("북마크 실패했습니다. 잠시 후 다시 시도해 주세요!"));
        }
    }
    // 투표 참여 리스트 조회
    @Transactional(readOnly = true)
    @Async
    public CompletableFuture<VoteHomeDataDto> findAll(Pageable pageable, String sort, String category, String searchTitle) {
         String[] sortFields = {"startDay","up","voting"};

         boolean contains = Arrays.asList(sortFields).contains(sort);
         if(!contains) {
             sort = "startDay";
         }
         int page = pageable.getPageNumber() - 1;
         if(pageable.getPageNumber() == 0) {
            page = 0;
         }

         if(category.isBlank()) {
             if(sort.equals("voting")) {
                 // 첫 번째 페이지, 10개
                 PageRequest pageRequest = PageRequest.of(page, 10);
                 Slice<Object[]> voteWithTotalCount = voteRepository.findVoteWithTotalCount(pageRequest);
                 List<VoteResponseDto> list = voteWithTotalCount.getContent().stream().map(v -> VoteResponseDto.createVoteResponseDto((Vote) v[0], (Long) v[1])).collect(Collectors.toList());
                 return CompletableFuture.completedFuture(VoteHomeDataDto.createVoteHomeDataDto(list,voteWithTotalCount.hasNext(),voteWithTotalCount.getNumber()));
             }
             PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Order.desc(sort)));
             Slice<Object[]> sliceVote = voteRepository.findAllByVote(pageRequest,searchTitle);
             List<VoteResponseDto> list = sliceVote.stream().map(v -> VoteResponseDto.createVoteResponseDto((Vote) v[0], (Long) v[1] )).collect(Collectors.toList());
             return CompletableFuture.completedFuture(VoteHomeDataDto.createVoteHomeDataDto(list,sliceVote.hasNext(),sliceVote.getNumber()));
         }
         else {
             Boolean checkCategory = category_enum.fromCode(category);
             if(!checkCategory) {
                 category = "ENTERTAINMENT";
             }
             if(sort.equals("voting")) {
                 // 첫 번째 페이지, 10개
                 PageRequest pageRequest = PageRequest.of(page, 10);
                 Slice<Object[]> voteWithTotalCount = voteRepository.findVotesWithTotalCountByCategory(pageRequest,category);
                 List<VoteResponseDto> list = voteWithTotalCount.getContent().stream().map(v -> VoteResponseDto.createVoteResponseDto((Vote) v[0], (Long) v[1] )).collect(Collectors.toList());
                 return CompletableFuture.completedFuture(VoteHomeDataDto.createVoteHomeDataDto(list,voteWithTotalCount.hasNext(),voteWithTotalCount.getNumber()));
             }
             PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Order.desc(sort)));
             Slice<Object[]> sliceVote = voteRepository.findAllByVoteAndCategory(pageRequest,category,searchTitle);
             List<VoteResponseDto> list = sliceVote.stream().map(v -> VoteResponseDto.createVoteResponseDto((Vote) v[0], (Long) v[1] )).collect(Collectors.toList());
             return CompletableFuture.completedFuture(VoteHomeDataDto.createVoteHomeDataDto(list,sliceVote.hasNext(),sliceVote.getNumber()));
         }
    }
    // 총 투표 수 , 인기 카테고리 조회
    @Transactional(readOnly = true)
    public HotCategoryAndTotalDto getSummary() {
        Object[] summary = voteRepository.findHotCategoryWithVoteCount();
        HotCategoryAndTotalDto hotCategoryAndTotalDto = HotCategoryAndTotalDto.createHotCategoryAndTotalDto(summary);
        return  hotCategoryAndTotalDto;
    }
    // 인기 투표 조회
    @Transactional(readOnly = true)
    public HotVoteandRankDto getHot() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Object[]> summary = voteRepository.findHotVote(pageRequest);
        List<HotVoteResponseDto> hotVoteResponseDto = summary.stream().map(v -> HotVoteResponseDto.createHotVoteResponseDto(v,0L)).collect(Collectors.toList());

        HotVoteandRankDto hotVoteandRankDto = HotVoteandRankDto.createHotVoteandRankDto(hotVoteResponseDto);
        return hotVoteandRankDto;
    }
    // 투표 추천 리스트 조회
    @Transactional(readOnly = true)
    public HotVoteandRankDto getRecommend() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Long user_id = AuthContext.getAuth() != null ? AuthContext.getAuth().getId() :null;
        List<Object[]> summary = voteRepository.findHotVotesExcludingUser(user_id,pageRequest);
        List<HotVoteResponseDto> hotVoteResponseDto = summary.stream().map(v -> HotVoteResponseDto.createHotVoteResponseDto(v,(Long) v[2])).collect(Collectors.toList());
        HotVoteandRankDto hotVoteandRankDto = HotVoteandRankDto.createHotVoteandRankDto(hotVoteResponseDto);
        return hotVoteandRankDto;

    }

}
