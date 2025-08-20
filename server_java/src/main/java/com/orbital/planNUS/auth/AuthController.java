package com.orbital.planNUS.auth;

import com.orbital.planNUS.config.JwtService;
import com.orbital.planNUS.token.RefreshTokenService;
import com.orbital.planNUS.user.Role;
import com.orbital.planNUS.user.User;
import com.orbital.planNUS.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authManager;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;

  public AuthController(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authManager,
      UserDetailsService userDetailsService,
      JwtService jwtService,
      RefreshTokenService refreshTokenService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authManager = authManager;
    this.userDetailsService = userDetailsService;
    this.jwtService = jwtService;
    this.refreshTokenService = refreshTokenService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
    if (userRepository.existsByEmail(req.email())) {
      return ResponseEntity.badRequest().body("Email already in use");
    }
    var user =
        User.builder()
            .email(req.email())
            .passwordHash(passwordEncoder.encode(req.password()))
            .roles(Set.of(Role.ROLE_USER))
            .enabled(true)
            .build();
    userRepository.save(user);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
    authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
    var userDetails = userDetailsService.loadUserByUsername(req.email());
    String accessToken = jwtService.generateAccessToken(userDetails);
    var user = userRepository.findByEmail(req.email()).orElseThrow();
    var refreshToken = refreshTokenService.mint(user);
    return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken(), user.getId()));
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody TokenRefreshRequest req) {
    var refreshToken = refreshTokenService.verifyUsable(req.refreshToken());
    var user = refreshToken.getUser();
    var userDetails = userDetailsService.loadUserByUsername(user.getEmail());
    String accessToken = jwtService.generateAccessToken(userDetails);
    // Optional rotation (recommended): revoke old, mint new
    refreshTokenService.revoke(refreshToken);
    var newRefreshToken = refreshTokenService.mint(user);
    return ResponseEntity.ok(
        new AuthResponse(accessToken, newRefreshToken.getToken(), user.getId()));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@Valid @RequestBody TokenRefreshRequest req) {
    var refreshToken = refreshTokenService.verifyUsable(req.refreshToken());
    refreshTokenService.revoke(refreshToken);
    return ResponseEntity.noContent().build();
  }
}
