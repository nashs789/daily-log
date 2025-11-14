package com.nashs.daily_log.api.post.controller;

import com.nashs.daily_log.api.post.api.PostApi;
import com.nashs.daily_log.api.post.request.PostRequest;
import com.nashs.daily_log.api.post.response.PostResponse;
import com.nashs.daily_log.application.post.PostFacade;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostRestController implements PostApi {

    private final PostFacade postFacade;
    private final PostService postService;

    @Override
    @PutMapping
    public ResponseEntity<PostResponse> savePost(
            @Valid @RequestBody PostRequest postRequest,
            LifeLogUser lifeLogUser
    ) {
        PostInfo postInfo = postRequest.toPostInfo()
                                       .setupUser(lifeLogUser);

        return ResponseEntity.ok(PostResponse.fromInfo(
                postFacade.savePost(lifeLogUser, postInfo)
        ));
    }

    @PatchMapping
    public ResponseEntity<Void> editPost(
            @Valid @RequestBody PostRequest postRequest,
            LifeLogUser lifeLogUser
    ) {
        PostInfo postInfo = postRequest.toPostInfo()
                                       .setupUser(lifeLogUser);

        postFacade.updatePost(lifeLogUser, postInfo);

        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            LifeLogUser lifeLogUser
    ) {
        postService.deletePostById(lifeLogUser, postId);

        return ResponseEntity.noContent()
                             .build();
    }
}
