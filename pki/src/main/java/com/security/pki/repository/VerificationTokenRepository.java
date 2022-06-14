package com.security.pki.repository;

import com.security.pki.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    VerificationToken findVerificationTokenByToken(String token);
}
