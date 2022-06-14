package com.security.pki.service.impl;

import com.security.pki.model.VerificationToken;
import com.security.pki.repository.VerificationTokenRepository;
import com.security.pki.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken saveVerificationToken(VerificationToken token) {
        return verificationTokenRepository.save(token);
    }

    @Override
    public VerificationToken findVerificationTokenByToken(String token) {
        return verificationTokenRepository.findVerificationTokenByToken(token);
    }

}

