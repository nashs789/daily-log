package com.nashs.daily_log.infra.post.repository.impl;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.info.CommentInfo;
import com.nashs.daily_log.infra.post.entity.Comment;
import com.nashs.daily_log.infra.post.entity.Post;
import com.nashs.daily_log.infra.post.repository.CommentJpaRepository;
import com.nashs.daily_log.infra.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nashs.daily_log.infra.post.entity.Comment.CommentStatus.DELETED;
import static com.nashs.daily_log.infra.post.entity.Comment.CommentStatus.UPDATED;

@Repository
@RequiredArgsConstructor
@Transactional
public class CommentRepositoryImpl {

    private final CommentJpaRepository commentJpaRepository;

    public Page<CommentInfo> findCommentOnPostWithoutReply(Long postId, Pageable pageable) {
        return commentJpaRepository.findCommentByPostIdAndStatusNotAndParentIsNullOrderByIdDesc(postId, DELETED, pageable)
                                   .map(Comment::toInfo);
    }

    public List<CommentInfo> findReplyOnComment(Long postId, List<CommentInfo> parent) {
        List<Long> parentIds = parent.stream()
                                     .map(CommentInfo::getId)
                                     .toList();

        return commentJpaRepository.findCommentByPostIdAndParentIdInAndStatusNotOrderByIdDesc(postId, parentIds, DELETED)
                                   .stream()
                                   .map(Comment::toInfo)
                                   .toList();
    }

    public CommentInfo saveCommentOnPost(LifeLogUser lifeLogUser, Long postId, CommentInfo commentInfo) {
        return commentJpaRepository.save(Comment.builder()
                                                .user(User.ref(lifeLogUser.sub()))
                                                .post(Post.ref(postId))
                                                .content(commentInfo.getContent())
                                                .build())
                                   .toInfo();
    }

    public CommentInfo saveCommentOnComment(LifeLogUser lifeLogUser, Long postId, CommentInfo commentInfo) {
        return commentJpaRepository.save(Comment.builder()
                                                 .build())
                                   .toInfo();
    }

    public boolean updateCommentOnPost(CommentInfo commentInfo) {
        return commentJpaRepository.updateCommentOnPost(commentInfo.getId(), commentInfo.getContent(), UPDATED) > 0;
    }

    public boolean deleteCommentOnPost(Long commentId) {
        return commentJpaRepository.deleteComment(commentId, DELETED) > 0;
    }
}
