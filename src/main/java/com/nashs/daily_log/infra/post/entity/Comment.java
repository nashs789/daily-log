package com.nashs.daily_log.infra.post.entity;

import com.nashs.daily_log.infra.common.entity.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Comment extends Timestamp {

    @Getter
    @RequiredArgsConstructor
    public enum CommentStatus {
        NORMAL,
        DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}
