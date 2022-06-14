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
public class CertificateDTO {


	private Integer id;
	private Boolean revoked;
	private Date validFrom;
	private Date validTo;
	private String issuerName; //email
	private String subjectName; //email
	private CertificateDataDTO certificateDataDTO;
	private String certificateType;
	private String certificateUsage;

}
