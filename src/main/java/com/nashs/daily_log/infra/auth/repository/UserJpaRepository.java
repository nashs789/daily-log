package com.nashs.daily_log.infra.auth.repository;

import com.nashs.daily_log.infra.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findBySub(String sub);
}
