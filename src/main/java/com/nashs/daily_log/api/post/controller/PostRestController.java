package com.nashs.daily_log.api.post.controller;

import com.nashs.daily_log.api.post.request.PostRequest;
import com.nashs.daily_log.api.post.response.PostResponse;
import com.nashs.daily_log.application.post.PostFacade;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.info.PostInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostRestController {

    private final PostFacade postFacade;

    @PutMapping
    public ResponseEntity<PostResponse> savePost(
            @Valid @RequestBody PostRequest postRequest,
            LifeLogUser lifeLogUser
    ) {
        PostInfo postInfo = postRequest.toPostInfo()
                                       .setupUser(lifeLogUser);

        return ResponseEntity.ok(PostResponse.fromInfo(
                postFacade.savePost(lifeLogUser, postInfo, postRequest.templateId())
        ));
    }
}
