package com.security.pki.repository;

import com.security.pki.enums.CertificateType;
import com.security.pki.model.MyCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<MyCertificate, Integer> {

    MyCertificate findMyCertificateBySerialNumber(String serialNumber);

    MyCertificate findBySerialNumber(String issuerSerialNumber);
}
