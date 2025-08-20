package com.orbital.planNUS.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
    @Email String email, @NotBlank String password) {}
