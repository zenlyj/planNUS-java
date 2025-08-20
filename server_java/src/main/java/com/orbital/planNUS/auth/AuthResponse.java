package com.orbital.planNUS.auth;

public record AuthResponse(String accessToken, String refreshToken, String tokenType, Long userId) {
  public AuthResponse(String accessToken, String refreshToken, Long userId) {
    this(accessToken, refreshToken, "Bearer", userId);
  }
}
