package com.nashs.daily_log.infra.template.entity;

import com.nashs.daily_log.infra.common.converter.StringToMapConverter;
import com.nashs.daily_log.infra.common.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "template")
public class Template extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK, not null, name=user_id
    private Long userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "json", nullable = false)
    private String content;

    @Column(name = "raw_content", columnDefinition = "text", nullable = false)
    private String rawContent;

    @Column(name = "params", columnDefinition = "jsonb", nullable = false)
    @Convert(converter = StringToMapConverter.class)
    private Map<String, String> params;
}
