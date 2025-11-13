package com.nashs.daily_log.infra.post.repository.impl;

import com.nashs.daily_log.ContainerTest;
import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.info.CommentInfo;
import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.infra.post.exception.CommentInfraException;
import com.nashs.daily_log.infra.user.entity.User;
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

import java.util.List;

import static com.nashs.daily_log.infra.post.entity.Comment.CommentStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
class CommentRepositoryImplTest extends ContainerTest {

    @Autowired
    private CommentRepositoryImpl commentRepository;

    @Test
    @DisplayName("단일 댓글 조회")
    void findCommentById() {
        // given
        final Long COMMENT_ID = 1L;

        // when
        CommentInfo commentInfo = commentRepository.findById(COMMENT_ID);

        // then
        assertThat(commentInfo)
                .isNotNull()
                .extracting(CommentInfo::getId)
                .isEqualTo(COMMENT_ID);
    }

    @Test
    @DisplayName("존재하지 않는 댓글 조회")
    void findNotExistedCommentById() {
        // given
        final Long NOT_EXISTED_COMMENT_ID = 999_999_999L;

        // when
        CommentInfraException ex = assertThrows(CommentInfraException.class, () -> {
            commentRepository.findById(NOT_EXISTED_COMMENT_ID);
        });

        // then
        assertThat(ex)
                .isInstanceOf(CommentInfraException.class)
                .extracting(CommentInfraException::getStatus)
                .isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("게시글에 작성된 댓글 조회")
    void findCommentOnPost() {
        // given
        final Long POST_ID = 1L;
        final int REQ_PAGE = 0;
        final int PAGE_SIZE = 10;
        Pageable pageable = PageRequest.of(REQ_PAGE, PAGE_SIZE);

        // when
        Page<CommentInfo> commentOnPost = commentRepository.findCommentOnPostWithoutReply(POST_ID, pageable);

        // then
        assertThat(commentOnPost.getContent())
                .isNotNull()
                .isNotEmpty()
                .allSatisfy(c -> {
                    assertThat(c.getStatus()).isNotEqualTo(DELETED);
                    assertThat(c.getPostInfo().getId()).isEqualTo(POST_ID);
                    assertThat(c.getParent()).isNull();
                });
        assertAll(() -> {
            assertEquals(REQ_PAGE, commentOnPost.getNumber());
            assertEquals(PAGE_SIZE, commentOnPost.getSize());
            assertThat(commentOnPost.getContent().stream()).hasSizeLessThanOrEqualTo(PAGE_SIZE);
        });
    }

    @Test
    @DisplayName("댓글에 해당하는 대댓글 조회")
    void findReplyOnComment() {
        // given
        final Long POST_ID = 1L;
        final int REQ_PAGE = 0;
        final int PAGE_SIZE = 10;
        Pageable pageable = PageRequest.of(REQ_PAGE, PAGE_SIZE);
        List<CommentInfo> commentOnPost = commentRepository.findCommentOnPostWithoutReply(POST_ID, pageable)
                                                           .getContent();

        // when
        List<CommentInfo> replyOnComment = commentRepository.findReplyOnComment(POST_ID, commentOnPost);

        // then
        assertThat(replyOnComment)
                .isNotNull()
                .isNotEmpty()
                .hasSize(4)
                .allSatisfy(c -> {
                    assertThat(c.getStatus()).isNotEqualTo(DELETED);
                    assertThat(c.getParent()).isEqualTo(POST_ID);
                    assertThat(c.getParent()).isNotNull();
                });
    }

    @Test
    @DisplayName("게시글 댓글 달기")
    void saveCommentOnPost() {
        // given
        final Long POST_ID = 1L;
        final String USER_SUB = "user1";
        UserInfo userInfo = User.ref(USER_SUB)
                                .toInfo();
        PostInfo postInfo = PostInfo.builder()
                                    .id(POST_ID)
                                    .userInfo(userInfo)
                                    .build();
        CommentInfo commentInfo = CommentInfo.builder()
                                             .userInfo(userInfo)
                                             .postInfo(postInfo)
                                             .parent(null)
                                             .content("content")
                                             .build();

        // when
        CommentInfo savedCommentOnPost = commentRepository.saveCommentOnPost(commentInfo);

        // then
        assertThat(savedCommentOnPost)
                .isNotNull()
                .satisfies(c -> {
                   assertEquals(POST_ID, c.getPostInfo().getId());
                   assertEquals(USER_SUB, c.getUserInfo().getSub());
                   assertEquals(NORMAL, c.getStatus());
                   assertNull(c.getParent());
                });
    }

    @Test
    @DisplayName("댓글에 댓글 달기")
    void saveCommentOnComment() {
        // given
        final Long COMMENT_ID = 1L;
        final Long POST_ID = 1L;
        final String USER_SUB = "user1";
        LifeLogUser lifeLogUser = LifeLogUser.ref(USER_SUB);
        UserInfo userInfo = User.ref(lifeLogUser.sub())
                                .toInfo();
        PostInfo postInfo = PostInfo.builder()
                                    .id(POST_ID)
                                    .userInfo(userInfo)
                                    .build();
        CommentInfo commentOnPost = CommentInfo.builder()
                                               .userInfo(userInfo)
                                               .postInfo(postInfo)
                                               .parent(COMMENT_ID)
                                               .content("content")
                                               .build();

        // when
        CommentInfo savedCommentOnPost = commentRepository.saveCommentOnComment(commentOnPost);

        // then
        assertThat(savedCommentOnPost)
                .isNotNull()
                .satisfies(c -> {
                    assertEquals(POST_ID, c.getPostInfo().getId());
                    assertEquals(COMMENT_ID, c.getParent());
                    assertEquals(USER_SUB, c.getUserInfo().getSub());
                    assertEquals(NORMAL, c.getStatus());
                    assertNotNull(c.getParent());
                });
    }

    @Test
    @DisplayName("댓글 수정")
    void updateCommentOnPost() {
        // given
        final Long POST_ID = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        CommentInfo commentInfo = commentRepository.findCommentOnPostWithoutReply(POST_ID, pageable)
                                                   .getContent()
                                                   .stream()
                                                   .findFirst()
                                                   .orElseThrow();
        Long commentId = commentInfo.getId();
        String beforeContent = commentInfo.getContent();

        commentInfo.setContent("new content");

        // when
        boolean res = commentRepository.updateCommentOnPost(commentInfo);

        // then
        CommentInfo updateComment = commentRepository.findCommentOnPostWithoutReply(POST_ID, pageable)
                                                     .getContent()
                                                     .stream()
                                                     .filter(e -> e.getId().equals(commentId))
                                                     .findAny()
                                                     .orElseThrow();

        assertTrue(res);
        assertThat(updateComment)
                .isNotNull()
                .satisfies(c -> {
                    assertNotEquals(beforeContent, c.getContent());
                    assertEquals(UPDATED, c.getStatus());
                    assertEquals(commentId, c.getId());
                    assertEquals(POST_ID, c.getPostInfo().getId());
                });
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteCommentOnPost() {
        // given
        final Long POST_ID = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        CommentInfo commentInfo = commentRepository.findCommentOnPostWithoutReply(POST_ID, pageable)
                                                   .getContent()
                                                   .stream()
                                                   .findFirst()
                                                   .orElseThrow();
        Long commentId = commentInfo.getId();

        // when
        boolean res = commentRepository.deleteCommentOnPost(commentId);

        // then
        List<CommentInfo> list = commentRepository.findCommentOnPostWithoutReply(POST_ID, pageable)
                                                  .getContent()
                                                  .stream()
                                                  .filter(e -> e.getId().equals(commentId))
                                                  .toList();

        assertTrue(res);
        assertThat(list).isNotNull();
    }
}