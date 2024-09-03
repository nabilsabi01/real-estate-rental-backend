package com.easystay.realestaterental.dto;

import com.easystay.realestaterental.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Long userId;
    private String email;
    private UserRole role;
}
