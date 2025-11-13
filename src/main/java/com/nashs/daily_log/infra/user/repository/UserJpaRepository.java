package com.nashs.daily_log.infra.user.repository;

import com.nashs.daily_log.infra.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, String> {
}
