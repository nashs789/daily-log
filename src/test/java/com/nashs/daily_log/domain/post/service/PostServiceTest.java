package com.nashs.daily_log.domain.post.service;

import com.nashs.daily_log.ContainerTest;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.exception.PostDomainException;
import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.infra.post.entity.Post.PostStatus;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
class PostServiceTest extends ContainerTest {

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("게시글 리스트 조회")
    void findAllNormalPost() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<PostInfo> allPost = postService.findAllPost(pageable);

        // then
        assertThat(allPost.getContent())
                .isNotNull()
                .isNotEmpty()
                .extracting(PostInfo::getStatus)
                .allMatch(PostStatus::isNormal);
    }

    @Test
    @DisplayName("나의 게시글 리스트 조회")
    void findMyAllNormalPost() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        LifeLogUser lifeLogUser = LifeLogUser.ref("user1");

        // when
        Page<PostInfo> myAllPost = postService.findMyAllPost(lifeLogUser, pageable);

        // then
        assertThat(myAllPost)
                .isNotNull()
                .isNotEmpty()
                .extracting(PostInfo::getStatus)
                .allMatch(PostStatus::isNormal);
    }

    @Test
    @DisplayName("본인 게시글 아닌 게시글 수정 시도")
    void tryUpdateNotOwnedPost() {
        // given
        final String LOGIN_USER = "user1";
        final String POST_OWNER = "user2";
        Pageable pageable = PageRequest.of(0, 10);
        LifeLogUser lifeLogUser = LifeLogUser.ref(LOGIN_USER);
        PostInfo postInfo = postService.findAllPost(pageable)
                                       .stream()
                                       .filter(e -> POST_OWNER.equals(e.getUserInfo().getSub()))
                                       .findFirst()
                                       .orElseGet(() -> PostInfo.builder().build());

        // when & then
        assertThatThrownBy(() -> postService.updatePostById(lifeLogUser, postInfo))
                .isInstanceOf(PostDomainException.class)
                .extracting("status")
                .isEqualTo(BAD_REQUEST);
    }

    @Test
    @DisplayName("본인 게시글 아닌 게시글 삭제 시도")
    void tryDeleteNotOwnedPost() {
        // given
        final String LOGIN_USER = "user1";
        final String POST_OWNER = "user2";
        Pageable pageable = PageRequest.of(0, 10);
        LifeLogUser lifeLogUser = LifeLogUser.builder()
                                             .sub(LOGIN_USER)
                                             .build();
        Long postId = postService.findAllPost(pageable)
                                 .stream()
                                 .filter(e -> POST_OWNER.equals(e.getUserInfo().getSub()))
                                 .findFirst()
                                 .orElseThrow()
                                 .getId();

        // when & then
        assertThatThrownBy(() -> postService.deletePostById(lifeLogUser, postId))
                .isInstanceOf(PostDomainException.class)
                .extracting("status")
                .isEqualTo(BAD_REQUEST);
    }
}
