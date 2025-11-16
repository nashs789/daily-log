package com.nashs.daily_log.infra.webhook.repository;

import com.nashs.daily_log.infra.webhook.entity.WebhookHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebhookJpaRepository extends JpaRepository<WebhookHistory, Long> {
    List<WebhookHistory> findAllByUserSub(String userId);
}
