package com.example.beantechrmd.Pojo.Requests;


import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record SignupRequest(@NotBlank String username, @NotBlank String password, Set<String> roles ) {
}
