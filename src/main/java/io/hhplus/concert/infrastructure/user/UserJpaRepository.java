package io.hhplus.concert.infrastructure.user;

import io.hhplus.concert.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
