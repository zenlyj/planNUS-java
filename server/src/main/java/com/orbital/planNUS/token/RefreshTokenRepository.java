package com.orbital.planNUS.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

  void deleteByUserId(Long userId);
}
