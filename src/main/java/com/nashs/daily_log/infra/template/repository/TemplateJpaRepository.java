package com.nashs.daily_log.infra.template.repository;

import com.nashs.daily_log.infra.user.entity.User;
import com.nashs.daily_log.infra.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface TemplateJpaRepository extends JpaRepository<Template, Long> {
    List<Template> findAllByUser(User user);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE Template t
           SET t.title = :title
             , t.rawContent = :rawContent
             , t.params = :params
             , t.discord = :discord
             , t.slack = :slack
         WHERE t.id = :id
    """)
    void updateTemplate(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("rawContent") String rawContent,
            @Param("params") Map<String, String> params,
            @Param("discord") String discord,
            @Param("slack") String slack
    );
}
