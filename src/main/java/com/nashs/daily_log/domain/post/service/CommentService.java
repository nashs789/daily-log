package com.nashs.daily_log.domain.post.service;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.exception.CommentDomainException;
import com.nashs.daily_log.domain.post.info.CommentInfo;
import com.nashs.daily_log.domain.post.info.CommentOnPostInfo;
import com.nashs.daily_log.domain.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nashs.daily_log.domain.post.exception.CommentDomainException.CommentDomainExceptionCode.NOT_COMMENT_OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentOnPostInfo findAllCommentOnPost(Long postId, Pageable pageable) {
        Page<CommentInfo> commentWithoutReply = commentRepository.findCommentOnPostWithoutReply(postId, pageable);
        List<CommentInfo> reply = commentRepository.findReplyOnComment(postId, commentWithoutReply.getContent());

        return CommentOnPostInfo.of(commentWithoutReply, reply);
    }

    public CommentInfo saveCommentOnPost(CommentInfo commentInfo) {
        return commentRepository.saveCommentOnPost(commentInfo);
    }

    public CommentInfo saveReplyOnComment(CommentInfo commentInfo) {
        return commentRepository.saveCommentOnComment(commentInfo);
    }

    public boolean updateCommentOnPost(LifeLogUser lifeLogUser, CommentInfo commentInfo) {
        checkIsCommentOwner(lifeLogUser, commentInfo);

        return commentRepository.updateCommentOnPost(commentInfo);
    }

    public boolean deleteCommentOnPost(LifeLogUser lifeLogUser, CommentInfo commentInfo) {
        checkIsCommentOwner(lifeLogUser, commentInfo);

        return commentRepository.deleteCommentOnPost(commentInfo.getId());
    }

    private void checkIsCommentOwner(LifeLogUser lifeLogUser, CommentInfo commentInfo) {
        CommentInfo savedCommentInfo = commentRepository.findById(commentInfo.getId());

        if (!savedCommentInfo.getUserInfo().getSub().equals(lifeLogUser.sub())) {
            throw new CommentDomainException(NOT_COMMENT_OWNER);
        }
    }
}
