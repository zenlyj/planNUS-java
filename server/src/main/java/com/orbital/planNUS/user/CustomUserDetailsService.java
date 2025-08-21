package com.orbital.planNUS.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository users;

  public CustomUserDetailsService(UserRepository users) {
    this.users = users;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var user =
        users
            .findByEmail(email)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found: %s".formatted(email)));
    Set<GrantedAuthority> authorities =
        user.getRoles().stream()
            .map(r -> new SimpleGrantedAuthority(r.name()))
            .collect(Collectors.toSet());
    return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
        .password(user.getPasswordHash())
        .authorities(authorities)
        .accountLocked(!user.isEnabled())
        .disabled(!user.isEnabled())
        .build();
  }
}
