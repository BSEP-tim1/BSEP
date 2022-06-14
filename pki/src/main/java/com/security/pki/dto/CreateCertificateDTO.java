package com.security.pki.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateCertificateDTO {
    private Date validFrom;
    private Date validTo;
    private String issuerName; //email
    private String subjectName; //email
    private CertificateDataDTO certificateDataDTO;
    private String certificateType;
    private String certificateUsage;
    private String issuerSerialNumber;
}
