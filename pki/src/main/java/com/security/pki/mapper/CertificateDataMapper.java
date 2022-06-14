package com.security.pki.mapper;

import com.security.pki.dto.CertificateDataDTO;
import com.security.pki.model.CertificateData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CertificateDataMapper {

    public CertificateData CertDataDtoToCertData(CertificateDataDTO dto) {
        CertificateData certificateData = new CertificateData();
        certificateData.setCommonName(dto.getCommonName());
        certificateData.setCountryCode(dto.getCountryCode());
        certificateData.setGivenName(dto.getGivenName());
        certificateData.setOrganization(dto.getOrganization());
        certificateData.setEmailAddress(dto.getEmailAddress());
        certificateData.setOrganizationalUnit(dto.getOrganizationalUnit());
        certificateData.setSurname(dto.getSurname());
        return certificateData;
    }

    public CertificateDataDTO certDataToCertDTO(CertificateData certificateData) {
        CertificateDataDTO dto = new CertificateDataDTO();
        dto.setCommonName(certificateData.getCommonName());
        dto.setCountryCode(certificateData.getCountryCode());
        dto.setGivenName(certificateData.getGivenName());
        dto.setOrganization(certificateData.getOrganization());
        dto.setEmailAddress(certificateData.getEmailAddress());
        dto.setOrganizationalUnit(certificateData.getOrganizationalUnit());
        dto.setSurname(certificateData.getSurname());
        return dto;
    }
}
