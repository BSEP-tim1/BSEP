package com.security.pki.dto;

import com.security.pki.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CertificateDataDTO {

    private String commonName;
    private String givenName;
    private String surname;
    private String organization;
    private String organizationalUnit;
    private String countryCode;
    private String emailAddress;
    private Integer userId;
}
