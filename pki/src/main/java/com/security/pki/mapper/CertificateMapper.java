package com.security.pki.mapper;

import com.security.pki.dto.AllCertificatesViewDTO;
import com.security.pki.dto.CreateCertificateDTO;
import com.security.pki.dto.CreateSelfSignedCertificateDTO;
import com.security.pki.enums.CertificateType;
import com.security.pki.model.MyCertificate;

import com.security.pki.dto.CertificateDTO;
import com.security.pki.model.User;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@NoArgsConstructor
public class CertificateMapper {
	
	public MyCertificate CertificateDtoToCertificate(CertificateDTO dto) {
		MyCertificate cert = new MyCertificate();
		cert.setId(dto.getId());
		cert.setRevoked(dto.getRevoked());
		cert.setValidFrom(dto.getValidFrom());
		cert.setValidTo(dto.getValidTo());

		return cert;
	}

	public CertificateDTO certificateToCertificateDto(MyCertificate certificate) {
		CertificateDTO dto = new CertificateDTO();
		dto.setRevoked(certificate.isRevoked());
		dto.setValidFrom(certificate.getValidFrom());
		dto.setValidTo(certificate.getValidTo());
		dto.setId(certificate.getId());
		CertificateDataMapper cdMapper = new CertificateDataMapper();
		dto.setCertificateDataDTO(cdMapper.certDataToCertDTO(certificate.getCertificateData()));
		dto.setCertificateType(certificate.getCertificateType().toString());
		dto.setCertificateUsage(dto.getCertificateUsage());		// TODO
		return dto;
	}

	public AllCertificatesViewDTO certificateWithCommonNameToCertificateDto(MyCertificate certificate) {
		AllCertificatesViewDTO dto = new AllCertificatesViewDTO();
		dto.setId(certificate.getId());
		dto.setCommonName(certificate.getCertificateData().getCommonName());
		dto.setValidFrom(convertDateToString(certificate.getValidFrom()));
		dto.setValidTo(convertDateToString(certificate.getValidTo()));
		dto.setEmail(certificate.getUser().getEmail());
		dto.setCertificateType(certificate.getCertificateType().toString());
		dto.setSerialNumber(certificate.getSerialNumber());
		Calendar today = Calendar.getInstance();
		today.clear(Calendar.HOUR); today.clear(Calendar.MINUTE); today.clear(Calendar.SECOND);
		dto.setIsValid(certificate.getValidTo().after(today.getTime()));
		dto.setRevoked(certificate.isRevoked());

		return dto;
	}

	public String convertDateToString(Date dt) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dateToString = df.format(dt);
		return dateToString;
	}

    public MyCertificate CreateCertificateDtoToCertificate(CreateSelfSignedCertificateDTO dto, User user) {
		MyCertificate cert = new MyCertificate();
		cert.setValidFrom(dto.getValidFrom());
		cert.setValidTo(dto.getValidTo());
		setCertificateType(dto.getCertificateType(), cert);
		cert.setRevoked(false);
		cert.setCertificateUsage(dto.getCertificateUsage()); // TODO: Sanja: dogovoriti se za namene sertifikata
		cert.setUser(user);
		cert.setCertificateData(new CertificateDataMapper().CertDataDtoToCertData(dto.getCertificateDataDTO()));
		return cert;
	}

	public MyCertificate CreateCertificateDtoToCertificate(CreateCertificateDTO dto, User user) {
		MyCertificate cert = new MyCertificate();
		cert.setValidFrom(dto.getValidFrom());
		cert.setValidTo(dto.getValidTo());
		setCertificateType(dto.getCertificateType(), cert);
		cert.setRevoked(false);
		cert.setCertificateUsage(dto.getCertificateUsage()); // TODO: Sanja: dogovoriti se za namene sertifikata
		cert.setUser(user);
		cert.setCertificateData(new CertificateDataMapper().CertDataDtoToCertData(dto.getCertificateDataDTO()));
		return cert;
	}

	private void setCertificateType(String certificateType, MyCertificate cert) {
		if(certificateType.equals("SELF_SIGNED")){
			cert.setCertificateType(CertificateType.SELF_SIGNED);
		} else if(certificateType.equals("INTERMEDIATE")) {
			cert.setCertificateType(CertificateType.INTERMEDIATE);
		} else {
			cert.setCertificateType(CertificateType.END_ENTITY);
		}
	}
}
