package com.orbital.planNUS.token;

import com.orbital.planNUS.exception.AppException;
import com.orbital.planNUS.user.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class RefreshTokenService {
  private final RefreshTokenRepository repo;
  private final long ttlMs;
  private final SecureRandom random = new SecureRandom();

  public RefreshTokenService(
      RefreshTokenRepository repo, @Value("${app.jwt.refresh-token-ttl-ms}") long ttlMs) {
    this.repo = repo;
    this.ttlMs = ttlMs;
  }

  public RefreshToken mint(User user) {
    var refreshToken =
        RefreshToken.builder()
            .token(generateToken())
            .user(user)
            .expiresAt(Instant.now().plusMillis(ttlMs))
            .revoked(false)
            .build();
    return repo.save(refreshToken);
  }

  public RefreshToken verifyUsable(String token) {
    var refreshToken =
        repo.findByTokenAndRevokedFalse(token)
            .orElseThrow(() -> new AppException("refreshToken", "Invalid refresh token"));
    if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
      throw new AppException("refreshToken", "Refresh token expired");
    }
    return refreshToken;
  }

  public void revoke(RefreshToken refreshToken) {
    refreshToken.setRevoked(true);
    repo.save(refreshToken);
  }

  private String generateToken() {
    var bytes = new byte[64];
    random.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
