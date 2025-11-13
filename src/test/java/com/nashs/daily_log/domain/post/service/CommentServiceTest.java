package com.nashs.daily_log.domain.post.service;

import com.nashs.daily_log.ContainerTest;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.exception.CommentDomainException;
import com.nashs.daily_log.domain.post.info.CommentInfo;
import com.nashs.daily_log.domain.user.info.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest
@ActiveProfiles("test")
@Sql(
        scripts = {
                "/test-data/user/user_insert.sql",
                "/test-data/template/template_insert.sql",
                "/test-data/post/post_insert.sql",
                "/test-data/post/comment_insert.sql",
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(encoding = "UTF-8")
)
@Sql(
        scripts = {
                "/test-data/user/user_cleanup.sql",
                "/test-data/template/template_cleanup.sql",
                "/test-data/post/post_cleanup.sql",
                "/test-data/post/comment_cleanup.sql",
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(encoding = "UTF-8")
)
class CommentServiceTest extends ContainerTest {

    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("본인이 작성하지 않은 댓글 수정 시도")
    void tryUpdateNotOwnedComment() {
        // given
        final String COMMENT_OWNER = "user1";
        final String LOGIN_USER = "user2";
        LifeLogUser lifeLogUser = LifeLogUser.ref(LOGIN_USER);
        CommentInfo commentInfo = CommentInfo.builder()
                                             .id(1L)
                                             .userInfo(UserInfo.ref(COMMENT_OWNER))
                                             .build();

        // when & then
        assertThatThrownBy(() -> commentService.updateCommentOnPost(lifeLogUser, commentInfo))
                .isInstanceOf(CommentDomainException.class)
                .extracting("status")
                .isEqualTo(BAD_REQUEST);
    }

    @Test
    @DisplayName("본인이 작성하지 않은 댓글 삭제 시도")
    void tryDeleteNotOwnedComment() {
        // given
        final String COMMENT_OWNER = "user1";
        final String LOGIN_USER = "user2";
        LifeLogUser lifeLogUser = LifeLogUser.ref(LOGIN_USER);
        CommentInfo commentInfo = CommentInfo.builder()
                                             .id(1L)
                                             .userInfo(UserInfo.ref(COMMENT_OWNER))
                                             .build();

        // when & then
        assertThatThrownBy(() -> commentService.deleteCommentOnPost(lifeLogUser, commentInfo))
                .isInstanceOf(CommentDomainException.class)
                .extracting("status")
                .isEqualTo(BAD_REQUEST);
    }
}