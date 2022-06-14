package com.security.pki.service;

import com.security.pki.model.VerificationToken;

public interface VerificationTokenService {
    public VerificationToken saveVerificationToken(VerificationToken token);
    public VerificationToken findVerificationTokenByToken(String token);
}
