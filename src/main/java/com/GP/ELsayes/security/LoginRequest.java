package com.GP.ELsayes.security;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull
    private String userName;
    @NotNull
    private String password;
}
