package com.security.pki.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.security.pki.enums.CertificateType;
import com.security.pki.model.MyCertificate;
import com.security.pki.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificateReviewDTO {

    private Integer id;
    private String commonName;
    private String givenName;
    private String surname;
    private String organization;
    private String organizationalUnit;
    private String countryCode;
    private String emailAddress;
    private boolean revoked;
    private String validFrom;
    private String validTo;
    private String user;
    private String certificateType;
    private String serialNumber;
    private String certificateUsage;

    public CertificateReviewDTO(MyCertificate m){
        this.id = m.getId();
        this.commonName = m.getCertificateData().getCommonName();
        this.givenName = m.getCertificateData().getGivenName();
        this.surname = m.getCertificateData().getSurname();
        this.organization = m.getCertificateData().getOrganization();
        this.organizationalUnit = m.getCertificateData().getOrganizationalUnit();
        this.countryCode = m.getCertificateData().getCountryCode();
        this.emailAddress = m.getCertificateData().getEmailAddress();
        this.revoked = m.isRevoked();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.validFrom = dateFormat.format(m.getValidFrom());
        this.validTo = dateFormat.format(m.getValidTo());
        this.user = m.getUser().getEmail();
        this.certificateType = m.getCertificateType().toString();
        this.serialNumber = m.getSerialNumber();
        this.certificateUsage = m.getCertificateUsage();
    }
}
