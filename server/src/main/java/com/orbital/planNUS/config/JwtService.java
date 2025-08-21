package com.orbital.planNUS.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
  private final SecretKey key;
  private final long accessTtlMs;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.access-token-ttl-ms}") long accessTtlMs) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(Base64Padding(secret)));
    this.accessTtlMs = accessTtlMs;
  }

  public String generateAccessToken(UserDetails user) {
    Instant now = Instant.now();
    return Jwts.builder()
        .setSubject(user.getUsername())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusMillis(accessTtlMs)))
        .addClaims(Map.of("roles", user.getAuthorities().stream().map(Object::toString).toList()))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return parseClaims(token).getSubject();
  }

  public boolean isTokenValid(String token, UserDetails user) {
    String username = extractUsername(token);
    return username.equals(user.getUsername()) && !isExpired(token);
  }

  private boolean isExpired(String token) {
    return parseClaims(token).getExpiration().before(new Date());
  }

  private Claims parseClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  // Allow plain (non-base64) secrets in dev by padding or encoding
  private static String Base64Padding(String s) {
    // If not base64, encode; if base64 missing padding, pad it.
    try {
      Decoders.BASE64.decode(s);
      return s; // already base64
    } catch (Exception ignored) {
      return java.util.Base64.getEncoder()
          .encodeToString(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
  }
}
