package com.ll.mbooks.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "username 을(를) 입력해주세요.")
    private String username;
    @NotEmpty(message = "password 을(를) 입력해주세요.")
    private String password;
}
