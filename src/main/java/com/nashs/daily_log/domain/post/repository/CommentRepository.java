package com.nashs.daily_log.domain.post.repository;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.info.CommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository {
    CommentInfo findById(Long commentId);
    Page<CommentInfo> findCommentOnPostWithoutReply(Long postId, Pageable pageable);
    List<CommentInfo> findReplyOnComment(Long postId, List<CommentInfo> parent);
    CommentInfo saveCommentOnPost(LifeLogUser lifeLogUser, Long postId, CommentInfo commentInfo);
    CommentInfo saveCommentOnComment(LifeLogUser lifeLogUser, Long postId, CommentInfo commentInfo);
    boolean updateCommentOnPost(CommentInfo commentInfo);
    boolean deleteCommentOnPost(Long commentId);
}
