package com.easystay.realestaterental.dto;

import com.easystay.realestaterental.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRole role;
    private String profilePictureUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}