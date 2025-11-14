package com.nashs.daily_log.domain.post.repository;

import com.nashs.daily_log.domain.post.info.CommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository {
    CommentInfo findById(Long commentId);
    Page<CommentInfo> findCommentOnPostWithoutReply(Long postId, Pageable pageable);
    Long countCommentOnPost(Long postId);
    List<CommentInfo> findReplyOnComment(Long postId, List<CommentInfo> parent);
    Long countReplyOnPost(Long parentId);
    CommentInfo saveCommentOnPost(CommentInfo commentInfo);
    CommentInfo saveCommentOnComment(CommentInfo commentInfo);
    boolean updateCommentOnPost(CommentInfo commentInfo);
    boolean deleteCommentOnPost(Long commentId);
}
