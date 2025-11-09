package com.nashs.daily_log.infra.post.repository;

import com.nashs.daily_log.infra.post.entity.Post;
import com.nashs.daily_log.infra.post.entity.Post.PostStatus;
import com.nashs.daily_log.infra.template.entity.Template;
import com.nashs.daily_log.infra.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Query(value = """
        SELECT p
          FROM Post p
         WHERE p.id = :id
    """)
    Optional<Post> findPostById(@Param("id") Long id);

    List<Post> findAllByOrderByIdDesc();

    List<Post> findByUserOrderByIdDesc(User user);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE Post p
           SET p.template = :template
             , p.title = :title
             , p.content = :content
         WHERE p.id = :id
    """)
    int updatePostById(
            @Param("id") Long id,
            @Param("template") Template template,
            @Param("title") String title,
            @Param("content") String content
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE Post p
           SET p.status = :status
         WHERE p.id = :id
     """)
    int deletePostById(
            @Param("id") Long id,
            @Param("status") PostStatus status
    );
}
