package com.nashs.daily_log.infra.post.repository.impl;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.info.CommentInfo;
import com.nashs.daily_log.infra.post.entity.Comment;
import com.nashs.daily_log.infra.post.entity.Comment.CommentStatus;
import com.nashs.daily_log.infra.post.entity.Post;
import com.nashs.daily_log.infra.post.repository.CommentJpaRepository;
import com.nashs.daily_log.infra.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentRepositoryImplUnitTest {

    @Mock
    private CommentJpaRepository commentJpaRepository;

    @InjectMocks
    private CommentRepositoryImpl commentRepository;

    @Test
    @DisplayName("Unit: 게시글에 작성된 댓글 조회")
    void findCommentOnPost() {
        // given
        User user = User.builder().build();
        Post post = Post.builder().user(user).build();
        List<Comment> list = List.of(
                Comment.builder().user(user).post(post).build(),
                Comment.builder().user(user).post(post).build(),
                Comment.builder().user(user).post(post).build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> page = new PageImpl<>(list, pageable, list.size());

        when(commentJpaRepository.findCommentByPostIdAndStatusNotAndParentIsNullOrderByIdDesc(anyLong(), any(CommentStatus.class), any(Pageable.class)))
                .thenReturn(page);

        // when
        Page<CommentInfo> commentList = commentRepository.findCommentOnPostWithoutReply(1L, pageable);

        // then
        assertThat(commentList.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
        verify(commentJpaRepository).findCommentByPostIdAndStatusNotAndParentIsNullOrderByIdDesc(anyLong(), any(CommentStatus.class), any(Pageable.class));
        verifyNoMoreInteractions(commentJpaRepository);
    }

    @Test
    @DisplayName("Unit: 댓글에 해당하는 대댓글 조회")
    void findReplyOnComment() {
        // given
        User user = User.builder().build();
        Post post = Post.builder().user(user).build();
        List<Comment> list = List.of(
                Comment.builder().user(user).post(post).build(),
                Comment.builder().user(user).post(post).build(),
                Comment.builder().user(user).post(post).build()
        );

        when(commentJpaRepository.findCommentByPostIdAndParentIdInAndStatusNotOrderByIdDesc(
                any(), anyList(), any(CommentStatus.class))
        ).thenReturn(list);

        // when
        List<CommentInfo> replyOnComment = commentRepository.findReplyOnComment(1L, List.of());

        // then
        assertThat(replyOnComment)
                .isNotEmpty()
                .isNotNull()
                .hasSize(list.size());
        verify(commentJpaRepository).findCommentByPostIdAndParentIdInAndStatusNotOrderByIdDesc(any(), anyList(), any(CommentStatus.class));
        verifyNoMoreInteractions(commentJpaRepository);
    }

    @Test
    @DisplayName("Unit: 게시글 댓글 달기")
    void saveCommentOnPost() {
        // given
        final Long POST_ID = 1L;
        final String USER = "user";
        LifeLogUser lifeLogUser = LifeLogUser.builder().sub(USER).build();
        CommentInfo commentInfo = CommentInfo.builder()
                                             .build();
        User user = User.ref(lifeLogUser.sub());
        Post post = Post.builder()
                        .id(POST_ID)
                        .user(user)
                        .build();
        Comment savedComment = Comment.builder()
                                      .user(user)
                                      .post(post)
                                      .build();

        when(commentJpaRepository.save(any(Comment.class)))
                .thenReturn(savedComment);

        // when
        CommentInfo savedCommentInfo = commentRepository.saveCommentOnPost(lifeLogUser, POST_ID, commentInfo);

        // then
        assertThat(savedCommentInfo)
                .isNotNull();
        assertEquals(POST_ID, savedCommentInfo.getPostInfo().getId());
        assertEquals(USER, savedCommentInfo.getUserInfo().getSub());
        verify(commentJpaRepository).save(any(Comment.class));
        verifyNoMoreInteractions(commentJpaRepository);
    }

    @Test
    @DisplayName("Unit: 댓글에 댓글 달기")
    void saveCommentOnComment() {
        // given
        final Long POST_ID = 1L;
        final Long PARENT_ID = 10L;
        final String USER = "user";
        LifeLogUser lifeLogUser = LifeLogUser.builder().sub(USER).build();
        User user = User.ref(lifeLogUser.sub());
        CommentInfo commentInfo = CommentInfo.builder()
                                             .userInfo(user.toInfo())
                                             .build();
        Post post = Post.builder()
                        .id(POST_ID)
                        .user(user)
                        .build();
        Comment parent = Comment.builder()
                                .id(PARENT_ID)
                                .user(user)
                                .post(post)
                                .build();
        Comment savedComment = Comment.builder()
                                      .user(user)
                                      .post(post)
                                      .parent(parent)
                                      .build();

        when(commentJpaRepository.save(any(Comment.class)))
                .thenReturn(savedComment);

        // when
        CommentInfo savedCommentInfo = commentRepository.saveCommentOnComment(lifeLogUser, POST_ID, commentInfo);

        // then
        assertThat(savedCommentInfo)
                .isNotNull();
        assertEquals(POST_ID, savedCommentInfo.getPostInfo().getId());
        assertEquals(USER, savedCommentInfo.getUserInfo().getSub());
        assertEquals(PARENT_ID, savedCommentInfo.getParent());
        verify(commentJpaRepository).save(any(Comment.class));
        verifyNoMoreInteractions(commentJpaRepository);
    }

    @Test
    @DisplayName("Unit: 댓글 수정")
    void updateCommentOnPost() {
        // given
        final Long COMMENT_ID = 1L;
        final String CONTENT = "Updated Comment";
        CommentInfo commentInfo = CommentInfo.builder()
                                             .id(COMMENT_ID)
                                             .content(CONTENT)
                                             .build();

        when(commentJpaRepository.updateCommentOnPost(anyLong(), anyString(), any(CommentStatus.class)))
                .thenReturn(1);

        // when
        boolean res = commentRepository.updateCommentOnPost(commentInfo);

        // then
        assertTrue(res);
        verify(commentJpaRepository).updateCommentOnPost(anyLong(), anyString(), any(CommentStatus.class));
        verifyNoMoreInteractions(commentJpaRepository);
    }

    @Test
    @DisplayName("Unit: 댓글 삭제")
    void deleteCommentOnPost() {
        // given
        final Long COMMENT_ID = 1L;

        when(commentJpaRepository.deleteComment(anyLong(), any(CommentStatus.class)))
                .thenReturn(1);

        // when
        boolean res = commentRepository.deleteCommentOnPost(COMMENT_ID);

        // then
        assertTrue(res);
        verify(commentJpaRepository).deleteComment(anyLong(), any(CommentStatus.class));
        verifyNoMoreInteractions(commentJpaRepository);
    }
}