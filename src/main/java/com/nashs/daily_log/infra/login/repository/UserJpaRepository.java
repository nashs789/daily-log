package com.nashs.daily_log.infra.login.repository;

import com.nashs.daily_log.infra.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findBySub(String sub);
}
