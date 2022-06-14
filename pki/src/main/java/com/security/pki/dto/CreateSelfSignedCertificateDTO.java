package com.security.pki.dto;

import com.security.pki.enums.CertificateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateSelfSignedCertificateDTO {
    private Date validFrom;
    private Date validTo;
    private String issuerName; //email
    private String subjectName; //email
    private CertificateDataDTO certificateDataDTO;
    private String certificateType;
    private String certificateUsage;
}
