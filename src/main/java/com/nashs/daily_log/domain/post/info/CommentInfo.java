package com.nashs.daily_log.domain.post.info;

import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.infra.post.entity.Comment.CommentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentInfo {
    private Long id;
    private UserInfo userInfo;
    private PostInfo postInfo;
    private Long parent;
    private String content;
    private CommentStatus status;
}
