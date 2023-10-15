package com.example.beantechrmd.Pojo.Responses;


import java.util.List;

public record UserAppInfoResponse(Integer id, String username, List<String> roles) {
}
