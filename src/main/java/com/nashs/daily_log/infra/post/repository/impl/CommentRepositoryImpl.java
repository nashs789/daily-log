package com.nashs.daily_log.infra.post.repository.impl;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.info.CommentInfo;
import com.nashs.daily_log.domain.post.repository.CommentRepository;
import com.nashs.daily_log.infra.post.entity.Comment;
import com.nashs.daily_log.infra.post.entity.Post;
import com.nashs.daily_log.infra.post.exception.CommentInfraException;
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
import static com.nashs.daily_log.infra.post.exception.CommentInfraException.CommentInfraExceptionCode.NO_SUCH_COMMENT;

@Repository
@RequiredArgsConstructor
@Transactional
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public CommentInfo findById(Long commentId) {
        return commentJpaRepository.findById(commentId)
                                   .orElseThrow(() -> new CommentInfraException(NO_SUCH_COMMENT))
                                   .toInfo();
    }

    @Override
    public Page<CommentInfo> findCommentOnPostWithoutReply(Long postId, Pageable pageable) {
        return commentJpaRepository.findCommentByPostIdAndParentIsNullOrderByIdDesc(postId, pageable)
                                   .map(Comment::toInfo);
    }

    @Override
    public List<CommentInfo> findReplyOnComment(Long postId, List<CommentInfo> parent) {
        List<Long> parentIds = parent.stream()
                                     .map(CommentInfo::getId)
                                     .toList();

        return commentJpaRepository.findCommentByPostIdAndParentIdInOrderByIdDesc(postId, parentIds)
                                   .stream()
                                   .map(Comment::toInfo)
                                   .toList();
    }

    @Override
    public CommentInfo saveCommentOnPost(CommentInfo commentInfo) {
        final String userSub = commentInfo.getUserInfo().getSub();

        return commentJpaRepository.save(Comment.builder()
                                                .user(User.ref(userSub))
                                                .post(Post.ref(commentInfo.getPostInfo().getId(), userSub))
                                                .content(commentInfo.getContent())
                                                .build())
                                   .toInfo();
    }

    @Override
    public CommentInfo saveCommentOnComment(CommentInfo commentInfo) {
        final String userSub = commentInfo.getUserInfo().getSub();

        return commentJpaRepository.save(Comment.builder()
                                                .user(User.ref(userSub))
                                                .post(Post.ref(commentInfo.getPostInfo().getId(), userSub))
                                                .content(commentInfo.getContent())
                                                .parent(Comment.ref(commentInfo.getParent()))
                                                .build())
                                   .toInfo();
    }

    @Override
    public boolean updateCommentOnPost(CommentInfo commentInfo) {
        return commentJpaRepository.updateCommentOnPost(commentInfo.getId(), commentInfo.getContent(), UPDATED) > 0;
    }

    @Override
    public boolean deleteCommentOnPost(Long commentId) {
        return commentJpaRepository.deleteComment(commentId, DELETED) > 0;
    }
}
