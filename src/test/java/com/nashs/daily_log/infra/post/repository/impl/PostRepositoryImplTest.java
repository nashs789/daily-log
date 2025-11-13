package com.nashs.daily_log.infra.post.repository.impl;

import com.nashs.daily_log.ContainerTest;
import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.template.info.TemplateInfo;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.infra.post.exception.PostInfraException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import static com.nashs.daily_log.infra.post.entity.Post.PostStatus.DELETED;
import static com.nashs.daily_log.infra.post.entity.Post.PostStatus.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest
@ActiveProfiles("test")
@Sql(
        scripts = {
                "/test-data/user/user_insert.sql",
                "/test-data/template/template_insert.sql",
                "/test-data/post/post_insert.sql",
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(encoding = "UTF-8")
)
@Sql(
        scripts = {
                "/test-data/user/user_cleanup.sql",
                "/test-data/template/template_cleanup.sql",
                "/test-data/post/post_cleanup.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(encoding = "UTF-8")
)
class PostRepositoryImplTest extends ContainerTest {

    @Autowired
    private PostRepositoryImpl postRepository;

    @Test
    @DisplayName("단일 게시글 조회")
    void findPostById() {
        // given
        final Long POST_ID = 1L;

        // when
        PostInfo postInfo = postRepository.findPostById(POST_ID);

        // then
        assertThat(postInfo)
                .isNotNull()
                .extracting(PostInfo::getId)
                .isEqualTo(POST_ID);
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void findNoSuchPostById() {
        // given
        final Long NOT_EXISTED_POST_ID = 999_999_999L;

        // when & then
        assertThatThrownBy(() -> postRepository.findPostById(NOT_EXISTED_POST_ID))
                .isInstanceOf(PostInfraException.class)
                .extracting("status")
                .isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("모든 게시글 조회")
    void findAllPost() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<PostInfo> allPost = postRepository.findAllPost(pageable);

        // then
        assertThat(allPost)
                .isNotNull()
                .isNotEmpty()
                .hasSize(6);
    }

    @Test
    @DisplayName("내가 작성한 게시글 조회")
    void findMyAllPost() {
        // given
        final String USER_SUB = "user1";
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<PostInfo> myAllPost = postRepository.findMyAllPost(pageable, USER_SUB);

        // then
        assertThat(myAllPost.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
    }

    @Test
    @DisplayName("게시글 생성")
    @Transactional
    void savePost() {
        // given
        final String USER_SUB = "user1";
        final String TITLE = "new post";
        final String CONTENT = "new post content";
        UserInfo userInfo = UserInfo.ref(USER_SUB);
        PostInfo postInfo = PostInfo.builder()
                                    .userInfo(userInfo)
                                    .templateInfo(TemplateInfo.builder()
                                                              .id(1L)
                                                              .userInfo(userInfo)
                                                              .build())
                                    .title("new post")
                                    .content("new post content")
                                    .build();

        // when
        PostInfo savedPost = postRepository.savePost(postInfo);

        // then
        assertThat(savedPost)
                .isNotNull()
                .extracting(PostInfo::getStatus)
                .isEqualTo(NORMAL);
        assertAll(() -> {
            assertEquals(USER_SUB, savedPost.getUserInfo().getSub());
            assertEquals(TITLE, savedPost.getTitle());
            assertEquals(CONTENT, savedPost.getContent());
        });
    }

    @Test
    @DisplayName("게시글 수정")
    void updatePostById() {
        // given
        final Long POST_ID = 1L;
        final String AFTER_TITLE = "new title";
        final String AFTER_CONTENT = "new content";
        PostInfo beforePostInfo = postRepository.findPostById(POST_ID);

        beforePostInfo.setTitle(AFTER_TITLE);
        beforePostInfo.setContent(AFTER_CONTENT);

        // when
        boolean res = postRepository.updatePostById(beforePostInfo);
        PostInfo afterPostInfo = postRepository.findPostById(POST_ID);

        // then
        assertAll(() -> {
            assertTrue(res);
            assertNotNull(afterPostInfo);
            assertEquals(AFTER_TITLE, afterPostInfo.getTitle());
            assertEquals(AFTER_CONTENT, afterPostInfo.getContent());
        });
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePostById() {
        // given
        final Long POST_ID = 1L;

        // when
        boolean res = postRepository.deletePostById(POST_ID);
        PostInfo deletedPost = postRepository.findPostById(POST_ID);

        // then
        assertAll(() -> {
            assertTrue(res);
            assertEquals(POST_ID, deletedPost.getId());
            assertEquals(DELETED, deletedPost.getStatus());
        });
    }
}