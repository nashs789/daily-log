package com.nashs.daily_log.infra.post.repository.impl;

import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.infra.post.entity.Post;
import com.nashs.daily_log.infra.post.entity.Post.PostStatus;
import com.nashs.daily_log.infra.post.exception.PostInfraException;
import com.nashs.daily_log.infra.post.repository.PostJpaRepository;
import com.nashs.daily_log.infra.template.entity.Template;
import com.nashs.daily_log.infra.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.nashs.daily_log.infra.post.entity.Post.PostStatus.DELETED;
import static com.nashs.daily_log.infra.post.entity.Post.PostStatus.NORMAL;
import static com.nashs.daily_log.infra.post.exception.PostInfraException.PostInfraExceptionCode.NO_SUCH_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostJpaRepositoryImplUnitTest {

    @Mock
    PostJpaRepository postJpaRepository;

    @InjectMocks
    PostRepositoryImpl postRepository;

    @Test
    @DisplayName("Unit: 단일 게시글 조회")
    void findPostById() {
        // given
        final Long POST_ID = 1L;
        Post post = Post.builder()
                        .id(POST_ID)
                        .user(User.builder().build())
                        .build();

        when(postJpaRepository.findPostById(anyLong()))
                .thenReturn(Optional.of(post));

        // when
        PostInfo postInfo = postRepository.findPostById(POST_ID);

        // then
        assertThat(postInfo)
                .isNotNull()
                .extracting(PostInfo::getId)
                .isEqualTo(POST_ID);
        verify(postJpaRepository).findPostById(POST_ID);
        verifyNoMoreInteractions(postJpaRepository);
    }

    @Test
    @DisplayName("Unit: 존재하지 않는 게시글 조회")
    void findNoSuchPostById() {
        // given
        final Long NOT_EXISTED_POST_ID = 999_999_999L;

        doThrow(new PostInfraException(NO_SUCH_POST))
                .when(postJpaRepository)
                .findPostById(NOT_EXISTED_POST_ID);

        // when & then
        assertThatThrownBy(() -> postRepository.findPostById(NOT_EXISTED_POST_ID))
                .isInstanceOf(PostInfraException.class);
        verify(postJpaRepository).findPostById(NOT_EXISTED_POST_ID);
        verifyNoMoreInteractions(postJpaRepository);
    }

    @Test
    @DisplayName("Unit: 모든 게시글 조회")
    void findAllPost() {
        // given
        User user1 = User.builder().build();
        User user2 = User.builder().build();
        User user3 = User.builder().build();
        List<Post> posts = List.of(
                Post.builder()
                    .user(user1)
                    .template(Template.builder().user(user1).build())
                    .build(),
                Post.builder()
                    .user(user2)
                    .template(Template.builder().user(user2).build())
                    .build(),
                Post.builder()
                    .user(user3)
                    .template(Template.builder().user(user3).build())
                    .build()
        );

        when(postJpaRepository.findAll()).thenReturn(posts);

        // when
        List<PostInfo> allPost = postRepository.findAllPost();

        // then
        assertThat(allPost)
                .isNotNull()
                .isNotEmpty()
                .hasSize(posts.size());
        verify(postJpaRepository).findAll();
        verifyNoMoreInteractions(postJpaRepository);
    }

    @Test
    @DisplayName("Unit: 내가 작성한 게시글 조회")
    void findMyAllPost() {
        // given
        final String USER_SUB = "user";
        User user = User.builder().build();
        List<Post> posts = List.of(
                Post.builder()
                    .user(user)
                    .template(Template.builder().user(user).build())
                    .build(),
                Post.builder()
                    .user(user)
                    .template(Template.builder().user(user).build())
                    .build()
        );

        when(postJpaRepository.findByUser(any(User.class)))
                .thenReturn(posts);

        // when
        List<PostInfo> myAllPost = postRepository.findMyAllPost(USER_SUB);

        // then
        assertThat(myAllPost)
                .isNotNull()
                .isNotEmpty()
                .hasSize(posts.size());
        verify(postJpaRepository).findByUser(any(User.class));
        verifyNoMoreInteractions(postJpaRepository);
    }

    @Test
    @DisplayName("Unit: 게시글 생성")
    void savePost() {
        // given
        final String USER_SUB = "user";
        User user = User.builder()
                        .sub(USER_SUB)
                        .build();
        PostInfo postInfo = PostInfo.builder()
                                    .userInfo(user.toInfo())
                                    .templateInfo(TemplateInfo.builder()
                                                              .userInfo(user.toInfo())
                                                              .build())
                                    .build();

        when(postJpaRepository.save(any(Post.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Post.class));

        // when
        PostInfo savedPostInfo = postRepository.savePost(postInfo);

        // then
        assertThat(savedPostInfo)
                .isNotNull();
        assertThat(savedPostInfo.getUserInfo().getSub())
                .isEqualTo(USER_SUB);
        verify(postJpaRepository).save(any(Post.class));
        verifyNoMoreInteractions(postJpaRepository);
    }

    @Test
    @DisplayName("Unit: 게시글 수정")
    void updatePostById() {
        // given
        final String TITLE = "TEST TITLE";
        final String CONTENT = "TEST CONTENT";
        PostInfo postInfo = PostInfo.builder()
                                    .id(1L)
                                    .title(TITLE)
                                    .content(CONTENT)
                                    .status(NORMAL)
                                    .build();

        when(postJpaRepository.updatePostById(
                anyLong(),
                any(),
                anyString(),
                anyString(),
                any(PostStatus.class)
        )).thenReturn(1);

        // when
        boolean res = postRepository.updatePostById(postInfo);

        // then
        assertTrue(res);
        verify(postJpaRepository).updatePostById(
                anyLong(),
                any(),
                eq(TITLE),
                eq(CONTENT),
                eq(NORMAL)
        );
        verifyNoMoreInteractions(postJpaRepository);
    }

    @Test
    @DisplayName("Unit: 게시글 삭제")
    void deletePostById() {
        // given
        PostInfo postInfo = PostInfo.builder()
                                    .id(1L)
                                    .status(DELETED)
                                    .build();

        when(postJpaRepository.deletePostById(anyLong(), any(PostStatus.class)))
                .thenReturn(1);

        // when
        boolean res = postRepository.deletePostById(postInfo);

        // then
        assertTrue(res);
        verify(postJpaRepository).deletePostById(
                anyLong(),
                eq(DELETED)
        );
        verifyNoMoreInteractions(postJpaRepository);
    }
}