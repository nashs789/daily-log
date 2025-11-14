package com.nashs.daily_log.domain.post.service;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.exception.PostDomainException;
import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.post.repository.PostRepository;
import com.nashs.daily_log.domain.user.info.UserInfo;
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

import static com.nashs.daily_log.infra.post.entity.Post.PostStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class PostServiceUnitTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("Unit: 게시글 리스트 조회")
    void findAllNormalPost() {
        // given
        List<PostInfo> returnPost = List.of(
                PostInfo.builder().status(NORMAL).build(),
                PostInfo.builder().status(NORMAL).build(),
                PostInfo.builder().status(NORMAL).build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<PostInfo> page = new PageImpl<>(returnPost, pageable, returnPost.size());

        when(postRepository.findAllPost(pageable))
                .thenReturn(page);

        // when
        Page<PostInfo> allPost = postService.findAllPost(pageable);

        // then
        int expectedSize = (int) returnPost.stream()
                                           .filter(e -> e.getStatus().isNormal())
                                           .count();

        assertThat(allPost)
                .isNotNull()
                .isNotEmpty()
                .hasSize(expectedSize);
        verify(postRepository).findAllPost(pageable);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("Unit: 나의 게시글 리스트 조회")
    void findMyAllNormalPost() {
        // given
        final String USER_SUB = "user1";
        LifeLogUser lifeLogUser = LifeLogUser.builder()
                                             .sub(USER_SUB)
                                             .build();
        Pageable pageable = PageRequest.of(0, 10);
        List<PostInfo> returnPost = List.of(
                PostInfo.builder().status(NORMAL).build(),
                PostInfo.builder().status(NORMAL).build(),
                PostInfo.builder().status(NORMAL).build(),
                PostInfo.builder().status(NORMAL).build()
        );
        Page<PostInfo> page = new PageImpl<>(returnPost, pageable, returnPost.size());

        when(postRepository.findMyAllPost(any(), anyString()))
                .thenReturn(page);

        // when
        Page<PostInfo> myAllPost = postService.findMyAllPost(lifeLogUser, pageable);

        // then
        int expectedSize = (int) returnPost.stream()
                                           .filter(e -> e.getStatus().isNormal())
                                           .count();

        assertThat(myAllPost)
                .isNotNull()
                .isNotEmpty()
                .hasSize(expectedSize);
        verify(postRepository).findMyAllPost(any(), anyString());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("Unit: 본인 게시글 아닌 게시글 수정 시도")
    void tryUpdateNotOwnedPost() {
        // given
        LifeLogUser lifeLogUser = LifeLogUser.builder()
                                             .sub("user1")
                                             .build();
        PostInfo savedPostInfo = PostInfo.builder()
                                         .userInfo(UserInfo.builder()
                                                           .sub("user2")
                                                           .build())
                                         .build();
        PostInfo postInfo = PostInfo.builder()
                                    .id(1L)
                                    .build();

        when(postRepository.findPostById(anyLong()))
                .thenReturn(savedPostInfo);

        // when
        PostDomainException ex = assertThrows(PostDomainException.class, () ->
                postService.updatePostById(lifeLogUser, postInfo)
        );

        // then
        assertEquals(BAD_REQUEST, ex.getStatus());
        verify(postRepository).findPostById(anyLong());
        verifyNoMoreInteractions(postRepository);
        verify(postRepository, never()).updatePostById(any(PostInfo.class));
    }

    @Test
    @DisplayName("Unit: 본인 게시글 아닌 게시글 삭제 시도")
    void tryDeleteNotOwnedPost() {
        // given
        LifeLogUser lifeLogUser = LifeLogUser.builder()
                                             .sub("user1")
                                             .build();
        PostInfo postInfo = PostInfo.builder()
                                    .userInfo(UserInfo.builder()
                                                      .sub("user2")
                                                      .build())
                                    .build();

        when(postRepository.findPostById(anyLong()))
                .thenReturn(postInfo);

        // when
        PostDomainException ex = assertThrows(PostDomainException.class, () ->
                postService.deletePostById(lifeLogUser, 1L)
        );

        // then
        assertEquals(BAD_REQUEST, ex.getStatus());
        verify(postRepository).findPostById(anyLong());
        verifyNoMoreInteractions(postRepository);
        verify(postRepository, never()).deletePostById(anyLong());
    }
}