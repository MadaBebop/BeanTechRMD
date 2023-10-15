package com.example.beantechrmd.Pojo.Requests;


import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record SingupRequest(@NotBlank String username, @NotBlank String password, Set<String> roles ) {
}
