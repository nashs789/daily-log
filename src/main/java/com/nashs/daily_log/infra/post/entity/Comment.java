package com.nashs.daily_log.infra.post.entity;

import com.nashs.daily_log.domain.post.info.CommentInfo;
import com.nashs.daily_log.infra.common.entity.Timestamp;
import com.nashs.daily_log.infra.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends Timestamp {

    @Getter
    @RequiredArgsConstructor
    public enum CommentStatus {
        NORMAL,
        UPDATED,
        DELETED,
        REPORTED
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(20) default 'NORMAL'")
    private CommentStatus status;

    public CommentInfo toInfo() {
        return CommentInfo.builder()
                          .id(id)
                          .userInfo(user.toInfo())
                          .postInfo(post.toInfo())
                          .parent(Objects.nonNull(parent) ? parent.toInfo() : null)
                          .content(content)
                          .status(status)
                          .build();
    }

    public static Comment ref(Long commentId) {
        return Comment.builder().id(commentId).build();
    }
}
