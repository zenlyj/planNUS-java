package com.orbital.planNUS.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @NotBlank(message = "Email is required") @Email String email,
    @NotBlank(message = "Password is required") String password) {}
