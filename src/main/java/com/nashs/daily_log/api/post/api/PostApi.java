package com.nashs.daily_log.api.post.api;

import com.nashs.daily_log.api.post.request.PostRequest;
import com.nashs.daily_log.api.post.response.PostResponse;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Post", description = "게시글 API")
public interface PostApi {

    @Operation(
            summary = "게시글 저장",
            description = "게시글을 생성 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 저장 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 오류 등 잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ResponseEntity<PostResponse> savePost(
            @Valid @RequestBody PostRequest postRequest,
            @Parameter(hidden = true)
            LifeLogUser lifeLogUser
    );
}
