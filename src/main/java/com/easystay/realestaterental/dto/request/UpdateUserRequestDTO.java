package com.easystay.realestaterental.dto.request;

import lombok.Data;

@Data
public class UpdateUserRequestDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
}
