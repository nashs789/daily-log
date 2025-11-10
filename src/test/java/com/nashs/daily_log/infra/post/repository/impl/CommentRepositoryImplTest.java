package com.nashs.daily_log.infra.post.repository.impl;

import com.nashs.daily_log.ContainerTest;
import com.nashs.daily_log.domain.post.info.CommentInfo;
import com.nashs.daily_log.infra.post.entity.Comment;
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
    @DisplayName("게시글에 작성된 댓글 조회")
    void findCommentOnPost() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        Page<CommentInfo> commentByPostOrderByIdDesc = commentRepository.findCommentOnPostWithoutReply(1L, pageable);

        for (CommentInfo comment : commentByPostOrderByIdDesc.getContent()) {
            System.out.println(comment.getContent() + " " + comment.getParent());
        }
        // when

        // then
    }

    @Test
    @DisplayName("댓글에 해당하는 대댓글 조회")
    void findReplyOnComment() {
        // given

        // when

        // then
    }
}