package com.security.pki.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllCertificatesViewDTO {

    private Integer id;
    private String certificateType;
    private String validFrom;
    private String validTo;
    private String commonName;
    private String email;
    private Boolean isValid;
    private boolean isRevoked;
    private String serialNumber;
}
