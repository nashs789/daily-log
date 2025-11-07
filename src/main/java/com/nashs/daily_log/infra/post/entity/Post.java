package com.nashs.daily_log.infra.post.entity;

import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.infra.common.entity.Timestamp;
import com.nashs.daily_log.infra.template.entity.Template;
import com.nashs.daily_log.infra.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

import static com.nashs.daily_log.infra.post.entity.Post.PostStatus.NORMAL;

@Getter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
public class Post extends Timestamp {

    @Getter
    @RequiredArgsConstructor
    public enum PostStatus {
        NORMAL,
        DELETED,
        HIDDEN;

        public boolean isNormal() {
            return this == NORMAL;
        }

        public boolean isDelete() {
            return this == DELETED;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_post_user")
    )
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(20) default 'NORMAL'")
    private PostStatus status = NORMAL;

    public PostInfo toInfo() {
        return PostInfo.builder()
                       .id(id)
                       .userInfo(user.toInfo())
                       .templateInfo(Objects.nonNull(template) ? template.toInfo() : null)
                       .title(title)
                       .content(content)
                       .status(status)
                       .build();
    }

    public static Post fromInfo(PostInfo info) {
        return Post.builder()
                   .id(info.getId())
                   .user(User.fromInfo(info.getUserInfo()))
                   .template(Template.fromInfo(info.getTemplateInfo()))
                   .title(info.getTitle())
                   .content(info.getContent())
                   .build();
    }
}
