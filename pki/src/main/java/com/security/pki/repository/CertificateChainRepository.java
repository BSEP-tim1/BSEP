package com.security.pki.repository;

import com.security.pki.model.CertificateChain;
import com.security.pki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateChainRepository extends JpaRepository<CertificateChain, Long> {
    List<CertificateChain> findByIssuerSerialNumber(String serialNumber);
}
