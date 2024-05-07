package com.auth.user.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SignuRequestDTO {

    private String email;
    private String password;
    private String name;
}
