package com.nashs.daily_log.domain.post.service;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.exception.CommentDomainException;
import com.nashs.daily_log.domain.post.info.CommentInfo;
import com.nashs.daily_log.domain.post.repository.CommentRepository;
import com.nashs.daily_log.infra.post.entity.Post;
import com.nashs.daily_log.infra.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class CommentServiceUnitTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("Unit: 본인이 작성하지 않은 댓글 수정 시도")
    void tryUpdateNotOwnedComment() {
        // given
        final Long COMMENT_ID = 1L;
        final String TRY_USER = "user";
        final String ANOTHER_USER = "Another User";
        LifeLogUser lifeLogUser = LifeLogUser.builder()
                                             .sub(TRY_USER)
                                             .build();
        User user = User.ref(ANOTHER_USER);
        CommentInfo updateCommentInfo = CommentInfo.builder()
                                                   .id(COMMENT_ID)
                                                   .userInfo(User.ref(TRY_USER).toInfo())
                                                   .build();
        CommentInfo savedCommentInfo = CommentInfo.builder()
                                                  .id(COMMENT_ID)
                                                  .userInfo(user.toInfo())
                                                  .postInfo(Post.ref(1L, ANOTHER_USER).toInfo())
                                                  .build();

        when(commentRepository.findById(anyLong()))
                .thenReturn(savedCommentInfo);

        // when
        CommentDomainException ex = assertThrows(CommentDomainException.class, () -> {
            commentService.updateCommentOnPost(lifeLogUser, updateCommentInfo);
        });


        // then
        assertThat(ex)
                .isNotNull()
                .extracting(CommentDomainException::getStatus)
                .isEqualTo(BAD_REQUEST);
    }

    @Test
    @DisplayName("Unit: 본인이 작성하지 않은 댓글 삭제 시도")
    void tryDeleteNotOwnedComment() {
        final Long COMMENT_ID = 1L;
        final String TRY_USER = "user";
        final String ANOTHER_USER = "Another User";
        LifeLogUser lifeLogUser = LifeLogUser.builder()
                                             .sub(TRY_USER)
                                             .build();
        User user = User.ref(ANOTHER_USER);
        CommentInfo deleteCommentInfo = CommentInfo.builder()
                                                   .id(COMMENT_ID)
                                                   .userInfo(User.ref(TRY_USER).toInfo())
                                                   .build();
        CommentInfo savedCommentInfo = CommentInfo.builder()
                                                  .id(COMMENT_ID)
                                                  .userInfo(user.toInfo())
                                                  .postInfo(Post.ref(1L, ANOTHER_USER).toInfo())
                                                  .build();

        when(commentRepository.findById(anyLong()))
                .thenReturn(savedCommentInfo);

        // when
        CommentDomainException ex = assertThrows(CommentDomainException.class, () -> {
            commentService.deleteCommentOnPost(lifeLogUser, deleteCommentInfo.getId());
        });


        // then
        assertThat(ex)
                .isNotNull()
                .extracting(CommentDomainException::getStatus)
                .isEqualTo(BAD_REQUEST);
    }
}