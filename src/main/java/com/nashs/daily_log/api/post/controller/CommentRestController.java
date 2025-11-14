package com.nashs.daily_log.api.post.controller;

import com.nashs.daily_log.api.post.request.CommentRequest;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.info.CommentInfo;
import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    @PutMapping
    public ResponseEntity<Void> saveCommentOnPost(
            LifeLogUser lifeLogUser,
            @RequestBody CommentRequest commentRequest
    ) {
        CommentInfo commentInfo = CommentInfo.builder()
                                             .userInfo(lifeLogUser.toUserInfo())
                                             .postInfo(PostInfo.ref(commentRequest.postId()))
                                             .content(commentRequest.content())
                                             .build();

        commentService.saveCommentOnPost(commentInfo);

        return ResponseEntity.ok()
                             .build();
    }

    @PutMapping("/reply")
    public ResponseEntity<Void> saveReplyOnComment(
            LifeLogUser lifeLogUser,
            @RequestBody CommentRequest commentRequest
    ) {
        CommentInfo commentInfo = CommentInfo.builder()
                                             .userInfo(lifeLogUser.toUserInfo())
                                             .postInfo(PostInfo.ref(commentRequest.postId()))
                                             .content(commentRequest.content())
                                             .parent(commentRequest.parentId())
                                             .build();

        commentService.saveReplyOnComment(commentInfo);

        return ResponseEntity.ok()
                             .build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentOnPost(
            LifeLogUser lifeLogUser,
            @PathVariable Long commentId
    ) {
        commentService.deleteCommentOnPost(lifeLogUser, commentId);

        return ResponseEntity.ok()
                             .build();
    }
}
