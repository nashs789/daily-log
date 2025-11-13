package com.nashs.daily_log.domain.post.info;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class CommentOnPostInfo {
    private Page<CommentInfo> comment;
    private Map<Long, List<CommentInfo>> reply;

    public static CommentOnPostInfo of(Page<CommentInfo> comment, List<CommentInfo> reply) {
        return CommentOnPostInfo.builder()
                                .comment(comment)
                                .reply(reply.stream()
                                            .collect(Collectors.groupingBy(CommentInfo::getParent))
                                )
                                .build();
    }

    public List<CommentInfo> getCommentList() {
        return comment.getContent();
    }
}
