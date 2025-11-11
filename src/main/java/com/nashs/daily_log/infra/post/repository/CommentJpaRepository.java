package com.nashs.daily_log.infra.post.repository;

import com.nashs.daily_log.infra.post.entity.Comment;
import com.nashs.daily_log.infra.post.entity.Comment.CommentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findOneById(Long id);
    Page<Comment> findCommentByPostIdAndStatusNotAndParentIsNullOrderByIdDesc(Long postId, CommentStatus status, Pageable pageable);
    List<Comment> findCommentByPostIdAndParentIdInAndStatusNotOrderByIdDesc(Long postId, List<Long> parentIds, CommentStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE Comment c
           SET c.status = :status
             , c.content = :content
         WHERE c.id = :commentId
    """)
    int updateCommentOnPost(
            @Param("commentId") Long commentId,
            @Param("content") String content,
            @Param("status") CommentStatus status
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE Comment c
           SET c.status = :status
         WHERE c.id = :commentId
    """)
    int deleteComment(
            @Param("commentId") Long commentId,
            @Param("status") CommentStatus status
    );
}
