package com.nashs.daily_log.api.post.request;

public record CommentRequest(
        Long parentId,
        Long postId,
        String content
) {
}
