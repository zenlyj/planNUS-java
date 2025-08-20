package com.orbital.planNUS.auth;

public record AuthResponse(String accessToken, String refreshToken, String tokenType) {
  public AuthResponse(String accessToken, String refreshToken) {
    this(accessToken, refreshToken, "Bearer");
  }
}
