package com.security.pki.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpUserDTO {
    public String email;

    public String password;

    public String userType;

    public String authorityType;
}
